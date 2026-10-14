BEGIN;

CREATE TEMP TABLE products_staging (
    sku           varchar(64),
    name          varchar(255),
    description   text,
    category_code varchar(100),
    brand         varchar(100),
    price         numeric(10, 2),
    attributes    text
) ON COMMIT DROP;

\copy products_staging (sku, name, description, category_code, brand, price, attributes) FROM 'database/seed/data/products.csv' WITH (FORMAT csv, HEADER true)

-- Fail loudly if any CSV row points to a category that doesn't exist.
    DO $$
DECLARE
missing text;
BEGIN
SELECT string_agg(DISTINCT s.category_code, ', ')
INTO missing
FROM products_staging s
         LEFT JOIN catalog.categories c ON c.code = s.category_code
WHERE c.id IS NULL;

IF missing IS NOT NULL THEN
        RAISE EXCEPTION 'Unknown category code(s) in products.csv: %', missing;
END IF;
END $$;

INSERT INTO catalog.products (sku, name, description, category_id, brand, price, attributes)
SELECT s.sku,
       s.name,
       s.description,
       c.id,
       s.brand,
       s.price,
       COALESCE(s.attributes::jsonb, '{}'::jsonb)
FROM products_staging s
         JOIN catalog.categories c ON c.code = s.category_code
    ON CONFLICT (sku) DO NOTHING;

COMMIT;