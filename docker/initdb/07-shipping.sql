CREATE SCHEMA IF NOT EXISTS shipping;

CREATE TABLE shipping.shipments (
    id              uuid PRIMARY KEY,
    order_id        uuid        NOT NULL UNIQUE,
    customer_id     uuid        NOT NULL,
    status          varchar(20) NOT NULL,
    tracking_number varchar(64) NOT NULL,
    created_at      timestamptz NOT NULL DEFAULT now()
);
