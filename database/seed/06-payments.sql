BEGIN;

CREATE TEMP TABLE payments_staging (
    order_ref varchar(32),
    status    varchar(20),
    reference varchar(100)
) ON COMMIT DROP;

\copy payments_staging (order_ref, status, reference) FROM 'database/seed/data/payments.csv' WITH (FORMAT csv, HEADER true)

-- Every payment must point to an order that exists.
    DO $$
DECLARE
unknown text;
BEGIN
SELECT string_agg(s.order_ref, ', ') INTO unknown
FROM payments_staging s
    LEFT JOIN orders.orders o ON o.id = md5('order:' || s.order_ref)::uuid
WHERE o.id IS NULL;

IF unknown IS NOT NULL THEN
        RAISE EXCEPTION 'payments.csv: unknown order_ref(s): %', unknown;
END IF;
END $$;

-- amount and customer_id come from the order itself, so they can't disagree with it.
INSERT INTO payment.payments (id, order_id, customer_id, amount, status, reference, created_at)
SELECT md5('payment:' || s.order_ref)::uuid,
    o.id,
       o.customer_id,
       o.total,
       s.status,
       s.reference,
       o.created_at
FROM payments_staging s
         JOIN orders.orders o ON o.id = md5('order:' || s.order_ref)::uuid
ON CONFLICT DO NOTHING;

COMMIT;