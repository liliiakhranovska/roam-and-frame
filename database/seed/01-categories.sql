BEGIN;

CREATE TEMP TABLE categories_staging (
    code        varchar(100),
    name        varchar(150),
    description text
) ON COMMIT DROP;

\copy categories_staging (code, name, description) FROM 'database/seed/data/categories.csv' WITH (FORMAT csv, HEADER true)

    INSERT INTO catalog.categories (code, name, description)
SELECT code, name, description
FROM categories_staging
    ON CONFLICT (code) DO NOTHING;

COMMIT;