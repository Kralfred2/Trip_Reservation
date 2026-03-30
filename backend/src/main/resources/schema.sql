INSERT INTO app_user (id, username, email, password, role)
VALUES (gen_random_uuid(), 'new_user', 'new@email.com', 'hashed_pass', 'ROLE_USER');