CREATE SCHEMA IF NOT EXISTS inventory;

CREATE TABLE inventory.stock_items (
    sku      varchar(64) PRIMARY KEY,
    quantity integer NOT NULL CHECK (quantity >= 0)
);

COPY inventory.stock_items (sku, quantity)
    FROM '/docker-entrypoint-initdb.d/data/stock.csv' WITH (FORMAT csv, HEADER true);
