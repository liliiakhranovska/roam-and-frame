CREATE SCHEMA IF NOT EXISTS catalog;

CREATE TABLE catalog.categories (
                                    id          uuid PRIMARY KEY DEFAULT gen_random_uuid(),
                                    code        varchar(100) NOT NULL UNIQUE,
                                    name        varchar(150) NOT NULL,
                                    description text
);

CREATE TABLE catalog.products (
                                  id          uuid PRIMARY KEY DEFAULT gen_random_uuid(),
                                  sku         varchar(64)  NOT NULL UNIQUE,
                                  name        varchar(255) NOT NULL,
                                  description text,
                                  category_id uuid NOT NULL REFERENCES catalog.categories (id),
                                  brand       varchar(100),
                                  price       numeric(10, 2) NOT NULL CHECK (price >= 0),
                                  attributes  jsonb NOT NULL DEFAULT '{}',
                                  active      boolean NOT NULL DEFAULT true,
                                  created_at  timestamptz NOT NULL DEFAULT now(),
                                  updated_at  timestamptz NOT NULL DEFAULT now()
);

CREATE INDEX products_category_id_idx ON catalog.products (category_id);

COPY catalog.categories (code, name, description)
    FROM '/docker-entrypoint-initdb.d/data/categories.csv' WITH (FORMAT csv, HEADER true);

CREATE TEMP TABLE products_staging (
    sku           varchar(64),
    name          varchar(255),
    description   text,
    category_code varchar(100),
    brand         varchar(100),
    price         numeric(10, 2),
    attributes    jsonb
);

COPY products_staging
    FROM '/docker-entrypoint-initdb.d/data/products.csv' WITH (FORMAT csv, HEADER true);

INSERT INTO catalog.products (sku, name, description, category_id, brand, price, attributes)
SELECT s.sku, s.name, s.description, c.id, s.brand, s.price, s.attributes
FROM products_staging s
         JOIN catalog.categories c ON c.code = s.category_code;

DROP TABLE products_staging;