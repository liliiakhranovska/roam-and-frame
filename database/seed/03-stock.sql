BEGIN;

CREATE TEMP TABLE stock_staging (
    sku      varchar(64),
    quantity integer
) ON COMMIT DROP;

\copy stock_staging (sku, quantity) FROM 'database/seed/data/stock.csv' WITH (FORMAT csv, HEADER true)

    -- inventory.stock_items has no foreign key to catalog.products (separate modules),
-- so a typo in a SKU would load without complaint. Check it here instead.
    DO $$
DECLARE
unknown text;
BEGIN
SELECT string_agg(s.sku, ', ')
INTO unknown
FROM stock_staging s
    LEFT JOIN catalog.products p ON p.sku = s.sku
WHERE p.id IS NULL;

IF unknown IS NOT NULL THEN
        RAISE EXCEPTION 'Stock rows reference unknown SKU(s): %', unknown;
END IF;
END $$;

INSERT INTO inventory.stock_items (sku, quantity)
SELECT sku, quantity
FROM stock_staging
    ON CONFLICT (sku) DO NOTHING;

COMMIT;