-- Customers
INSERT INTO customers (id, name, email, phone_number)
VALUES
    ('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 'Cliente 1', 'cliente1@exemplo.com', '11999990001'),
    ('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', 'Cliente 2', 'cliente2@exemplo.com', '11999990002'),
    ('cccccccc-cccc-cccc-cccc-cccccccccccc', 'Cliente 3', 'cliente3@exemplo.com', '11999990003');

-- Products (explicit ids to keep deterministic references for seeds)
INSERT INTO products (id, name, description, price, image_url, is_active)
VALUES
    (1, 'Heineken Lata', 'Cerveja Heineken lata 350ml', 8.90, 'https://imagebucket.com/img1.jpg', TRUE),
    (2, 'Coca-Cola Lata Zero', 'Refrigerante lata 350ml', 6.50, 'https://imagebucket.com/img2.jpg', TRUE),
    (3, 'Amstel Lata', 'Cerveja Amstel lata 350ml', 6.00, 'https://imagebucket.com/img3.jpg', TRUE),
    (4, 'Espeto Assado', 'Espeto de carne assado', 8.00, 'https://imagebucket.com/img4.jpg', TRUE),
    (5, 'Água', 'Água sem gás 500ml', 4.00, 'https://imagebucket.com/img5.jpg', TRUE),
    (6, 'Batata Frita', 'Porção individual', 14.90, 'https://imagebucket.com/img6.jpg', TRUE),
    (7, 'Hambúrguer', 'Pão, carne e queijo', 29.90, 'https://imagebucket.com/img7.jpg', TRUE),
    (8, 'Sobremesa', 'Pudim de leite', 12.90, 'https://imagebucket.com/img8.jpg', TRUE);

-- Keep sequences in sync after explicit ids
SELECT setval(pg_get_serial_sequence('products', 'id'), (SELECT MAX(id) FROM products));

-- Orders
INSERT INTO orders (id, status, opened_at, closed_at, customer_id)
VALUES
    (1, 'OPEN', CURRENT_TIMESTAMP - INTERVAL '10 minutes', NULL, 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa'),
    (2, 'CLOSED', CURRENT_TIMESTAMP - INTERVAL '2 days', CURRENT_TIMESTAMP - INTERVAL '2 days' + INTERVAL '45 minutes', 'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb'),
    (3, 'CLOSED', CURRENT_TIMESTAMP - INTERVAL '7 days', CURRENT_TIMESTAMP - INTERVAL '7 days' + INTERVAL '30 minutes', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa'),
    (4, 'OPEN', CURRENT_TIMESTAMP - INTERVAL '2 minutes', NULL, 'cccccccc-cccc-cccc-cccc-cccccccccccc'),
    (5, 'CLOSED', CURRENT_TIMESTAMP - INTERVAL '1 days', CURRENT_TIMESTAMP - INTERVAL '1 days' + INTERVAL '1 hour', 'cccccccc-cccc-cccc-cccc-cccccccccccc');

SELECT setval(pg_get_serial_sequence('orders', 'id'), (SELECT MAX(id) FROM orders));

-- Order items (PK: order_id + product_id). Produto não repete no pedido.
INSERT INTO order_items (order_id, product_id, quantity, unit_price, observations)
VALUES
    -- Order 1 (OPEN)
    (1, 2, 2, 6.50, 'Sem gelo'),
    (1, 4, 3, 8.00, 'Bem passado'),
    (1, 6, 1, 14.90, 'Bem crocante'),
    (1, 8, 1, 12.90, NULL),

    -- Order 2 (CLOSED)
    (2, 7, 1, 29.90, 'Ponto da carne: ao ponto'),
    (2, 5, 1, 4.00, NULL),
    (2, 1, 2, 8.90, 'Gelada'),

    -- Order 3 (CLOSED)
    (3, 4, 5, 8.00, 'Sem pimenta'),
    (3, 3, 6, 6.00, NULL),

    -- Order 4 (OPEN)
    (4, 7, 2, 29.90, '1 sem queijo'),
    (4, 2, 1, 6.50, NULL),
    (4, 6, 1, 14.90, 'Sem sal'),
    (4, 5, 2, 4.00, NULL),

    -- Order 5 (CLOSED)
    (5, 1, 4, 8.90, 'para o carlinhos'),
    (5, 4, 4, 8.00, '2 bem passados'),
    (5, 8, 2, 12.90, 'Uma porção com calda extra');
