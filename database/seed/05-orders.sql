BEGIN;

CREATE TEMP TABLE orders_staging (
    order_ref      varchar(32),
    customer_email varchar(255),
    status         varchar(20),
    created_at     timestamptz
) ON COMMIT DROP;

CREATE TEMP TABLE order_items_staging (
    row_id    serial,
    order_ref varchar(32),
    sku       varchar(64),
    quantity  integer
) ON COMMIT DROP;

\copy orders_staging (order_ref, customer_email, status, created_at) FROM 'database/seed/data/orders.csv' WITH (FORMAT csv, HEADER true)
    \copy order_items_staging (order_ref, sku, quantity) FROM 'database/seed/data/order_items.csv' WITH (FORMAT csv, HEADER true)

-- Validate the CSVs against each other and against existing data. Any failure aborts everything.
DO $$
DECLARE
problem text;
BEGIN
SELECT string_agg(DISTINCT o.customer_email, ', ') INTO problem
FROM orders_staging o
         LEFT JOIN customer.customers c ON c.email = lower(o.customer_email)
WHERE c.id IS NULL;
IF problem IS NOT NULL THEN
        RAISE EXCEPTION 'orders.csv: unknown customer email(s): %', problem;
END IF;

SELECT string_agg(DISTINCT i.sku, ', ') INTO problem
FROM order_items_staging i
         LEFT JOIN catalog.products p ON p.sku = i.sku
WHERE p.id IS NULL;
IF problem IS NOT NULL THEN
        RAISE EXCEPTION 'order_items.csv: unknown SKU(s): %', problem;
END IF;

SELECT string_agg(DISTINCT i.order_ref, ', ') INTO problem
FROM order_items_staging i
         LEFT JOIN orders_staging o ON o.order_ref = i.order_ref
WHERE o.order_ref IS NULL;
IF problem IS NOT NULL THEN
        RAISE EXCEPTION 'order_items.csv: order_ref(s) missing from orders.csv: %', problem;
END IF;

SELECT string_agg(o.order_ref, ', ') INTO problem
FROM orders_staging o
WHERE NOT EXISTS (SELECT 1 FROM order_items_staging i WHERE i.order_ref = o.order_ref);
IF problem IS NOT NULL THEN
        RAISE EXCEPTION 'orders.csv: order(s) without any items: %', problem;
END IF;
END $$;

-- Orders: id derived from order_ref, total computed from the items at current catalog prices.
INSERT INTO orders.orders (id, customer_id, status, total, created_at)
SELECT md5('order:' || o.order_ref)::uuid,
    c.id,
       o.status,
       t.total,
       o.created_at
FROM orders_staging o
         JOIN customer.customers c ON c.email = lower(o.customer_email)
         JOIN (
    SELECT i.order_ref, sum(p.price * i.quantity) AS total
    FROM order_items_staging i
             JOIN catalog.products p ON p.sku = i.sku
    GROUP BY i.order_ref
) t ON t.order_ref = o.order_ref
    ON CONFLICT (id) DO NOTHING;

-- Items: name and unit price are snapshots of the catalog, line_no follows CSV order within each order.
INSERT INTO orders.order_items (id, order_id, line_no, sku, name, unit_price, quantity, line_total)
SELECT md5('item:' || i.order_ref || ':' || i.line_no)::uuid,
    md5('order:' || i.order_ref)::uuid,
    i.line_no,
       i.sku,
       p.name,
       p.price,
       i.quantity,
       p.price * i.quantity
FROM (
         SELECT s.*, row_number() OVER (PARTITION BY s.order_ref ORDER BY s.row_id) AS line_no
         FROM order_items_staging s
     ) i
         JOIN catalog.products p ON p.sku = i.sku
    ON CONFLICT (id) DO NOTHING;

COMMIT;