CREATE SCHEMA IF NOT EXISTS customer;

CREATE EXTENSION IF NOT EXISTS pgcrypto;

CREATE FUNCTION customer.hash_pass(raw text) RETURNS text
    LANGUAGE sql
    AS $$ SELECT crypt(raw, gen_salt('bf', 10)) $$;

CREATE TABLE customer.customers (
                                    id              uuid PRIMARY KEY DEFAULT gen_random_uuid(),
                                    email           varchar(255) NOT NULL UNIQUE,
                                    password_hash   varchar(255) NOT NULL,
                                    first_name      varchar(100) NOT NULL,
                                    last_name       varchar(100) NOT NULL,
                                    phone           varchar(30),
                                    address_line1   varchar(255),
                                    address_line2   varchar(255),
                                    city            varchar(100),
                                    region          varchar(100),
                                    postal_code     varchar(20),
                                    country_code    char(2),
                                    created_at      timestamptz NOT NULL DEFAULT now(),
                                    updated_at      timestamptz NOT NULL DEFAULT now()
);

INSERT INTO customer.customers (email, password_hash, first_name, last_name)
VALUES ('lily.swan@gmail.com', customer.hash_pass('password'), 'Lily', 'Swan');