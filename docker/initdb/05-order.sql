CREATE SCHEMA IF NOT EXISTS orders;

CREATE TABLE orders.orders (
    id          uuid PRIMARY KEY,
    customer_id uuid           NOT NULL,
    status      varchar(20)    NOT NULL,
    total       numeric(12, 2) NOT NULL CHECK (total >= 0),
    created_at  timestamptz    NOT NULL DEFAULT now()
);

CREATE INDEX orders_customer_id_idx ON orders.orders (customer_id, created_at DESC);

CREATE TABLE orders.order_items (
    id         uuid PRIMARY KEY,
    order_id   uuid           NOT NULL REFERENCES orders.orders (id),
    line_no    integer        NOT NULL,
    sku        varchar(64)    NOT NULL,
    name       varchar(255)   NOT NULL,
    unit_price numeric(10, 2) NOT NULL,
    quantity   integer        NOT NULL CHECK (quantity > 0),
    line_total numeric(12, 2) NOT NULL
);

CREATE INDEX order_items_order_id_idx ON orders.order_items (order_id);
