ALTER TABLE customer.customers
    ADD COLUMN role varchar(20) NOT NULL DEFAULT 'CUSTOMER'
        CHECK (role IN ('CUSTOMER', 'ADMIN'));