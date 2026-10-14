BEGIN;

CREATE TEMP TABLE shipments_staging (
    order_ref       varchar(32),
    status          varchar(20),
    tracking_number varchar(64)
) ON COMMIT DROP;

\copy shipments_staging (order_ref, status, tracking_number) FROM 'database/seed/data/shipments.csv' WITH (FORMAT csv, HEADER true)

-- Every shipment must point to an order that exists.
    DO $$
DECLARE
unknown text;
BEGIN
SELECT string_agg(s.order_ref, ', ') INTO unknown
FROM shipments_staging s
    LEFT JOIN orders.orders o ON o.id = md5('order:' || s.order_ref)::uuid
WHERE o.id IS NULL;

IF unknown IS NOT NULL THEN
        RAISE EXCEPTION 'shipments.csv: unknown order_ref(s): %', unknown;
END IF;
END $$;

INSERT INTO shipping.shipments (id, order_id, customer_id, status, tracking_number, created_at)
SELECT md5('shipment:' || s.order_ref)::uuid,
    o.id,
       o.customer_id,
       s.status,
       s.tracking_number,
       o.created_at
FROM shipments_staging s
         JOIN orders.orders o ON o.id = md5('order:' || s.order_ref)::uuid
ON CONFLICT DO NOTHING;

COMMIT;