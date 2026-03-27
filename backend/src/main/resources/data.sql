-- 1. Insert Roles
INSERT INTO roles (name) VALUES ('ROLE_ADMIN') ON CONFLICT DO NOTHING;
INSERT INTO roles (name) VALUES ('ROLE_USER') ON CONFLICT DO NOTHING;

-- 2. Insert an Admin User
-- The password hash below is the BCrypt version of "password123"
INSERT INTO users (username, email, password_hash, role_id)
VALUES (
           'admin',
           'admin@example.com',
           '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.TVuHOnu',
           (SELECT id FROM roles WHERE name = 'ROLE_ADMIN')
       ) ON CONFLICT DO NOTHING;