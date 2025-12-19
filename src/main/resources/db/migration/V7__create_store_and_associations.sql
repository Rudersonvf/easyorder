CREATE TABLE stores (
    id UUID PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    slug VARCHAR(255) NOT NULL UNIQUE,
    cnpj VARCHAR(20),
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO stores (id, name, slug, cnpj, active)
VALUES ('dddddddd-dddd-dddd-dddd-dddddddddddd', 'Loja Padrao', 'loja-padrao', NULL, TRUE);

ALTER TABLE products ADD COLUMN store_id UUID;
ALTER TABLE orders ADD COLUMN store_id UUID;
ALTER TABLE customers ADD COLUMN store_id UUID;
ALTER TABLE users ADD COLUMN store_id UUID;

UPDATE products SET store_id = 'dddddddd-dddd-dddd-dddd-dddddddddddd';
UPDATE orders SET store_id = 'dddddddd-dddd-dddd-dddd-dddddddddddd';
UPDATE customers SET store_id = 'dddddddd-dddd-dddd-dddd-dddddddddddd';
UPDATE users SET store_id = 'dddddddd-dddd-dddd-dddd-dddddddddddd';

ALTER TABLE products ALTER COLUMN store_id SET NOT NULL;
ALTER TABLE orders ALTER COLUMN store_id SET NOT NULL;
ALTER TABLE customers ALTER COLUMN store_id SET NOT NULL;
ALTER TABLE users ALTER COLUMN store_id SET NOT NULL;

ALTER TABLE products
    ADD CONSTRAINT fk_products_store FOREIGN KEY (store_id) REFERENCES stores (id);
ALTER TABLE orders
    ADD CONSTRAINT fk_orders_store FOREIGN KEY (store_id) REFERENCES stores (id);
ALTER TABLE customers
    ADD CONSTRAINT fk_customers_store FOREIGN KEY (store_id) REFERENCES stores (id);
ALTER TABLE users
    ADD CONSTRAINT fk_users_store FOREIGN KEY (store_id) REFERENCES stores (id);

CREATE INDEX idx_products_store_id ON products (store_id);
CREATE INDEX idx_orders_store_id ON orders (store_id);
CREATE INDEX idx_customers_store_id ON customers (store_id);
CREATE INDEX idx_users_store_id ON users (store_id);
