-- Seed default roles
INSERT INTO roles (authority)
VALUES ('ADMIN'), ('USER');

-- Seed admin user with provided bcrypt password hash
INSERT INTO users (id, first_name, last_name, email, password)
VALUES (
    '11111111-1111-1111-1111-111111111111',
    'Admin',
    'User',
    'admin@admin.com',
    '$2a$12$GhaqAa1pj2GTosa6jL4S8ODVEzC6hWw.iH7UsM4GMPufx7phfLe0W'
);

-- Link admin user to ADMIN and USER roles
INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id
FROM users u
CROSS JOIN roles r
WHERE u.email = 'admin@admin.com'
  AND r.authority IN ('ADMIN', 'USER');
