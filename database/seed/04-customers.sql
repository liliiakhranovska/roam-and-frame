BEGIN;

CREATE TEMP TABLE customers_staging (
    email         varchar(255),
    password      text,
    first_name    varchar(100),
    last_name     varchar(100),
    phone         varchar(30),
    address_line1 varchar(255),
    address_line2 varchar(255),
    city          varchar(100),
    region        varchar(100),
    postal_code   varchar(20),
    country_code  varchar(2)
) ON COMMIT DROP;

\copy customers_staging (email, password, first_name, last_name, phone, address_line1, address_line2, city, region, postal_code, country_code) FROM 'database/seed/data/customers.csv' WITH (FORMAT csv, HEADER true)

    INSERT INTO customer.customers
    (email, password_hash, first_name, last_name, phone,
    address_line1, address_line2, city, region, postal_code, country_code)
SELECT lower(email),
       customer.hash_pass(password),
       first_name,
       last_name,
       phone,
       address_line1,
       address_line2,
       city,
       region,
       postal_code,
       country_code
FROM customers_staging
    ON CONFLICT (email) DO NOTHING;

COMMIT;