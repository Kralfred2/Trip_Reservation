INSERT INTO app_user (id, username, password, email)
VALUES (gen_random_uuid(), 'admin', 'your_bcrypt_hash_here', 'admin@example.com')
    ON CONFLICT (username) DO NOTHING;
INSERT INTO app_user (id, username, password, email)
VALUES (gen_random_uuid(), 'Alfred', 'hes', 'admin@a.cz')
    ON CONFLICT (username) DO NOTHING;
INSERT INTO app_user (id, username, password, email)
VALUES (gen_random_uuid(), 'Alfrsed', 'hses', 'admsin@a.cz')
    ON CONFLICT (username) DO NOTHING;