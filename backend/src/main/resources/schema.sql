INSERT INTO app_user (id, username, email, password, role)
VALUES (gen_random_uuid(), 'new_user', 'new@email.com', 'hashed_pass', 'ROLE_USER');

INSERT INTO app_user (id, username, email, password, role)
VALUES (gen_random_uuid(), 'adn@a', 'adn@a', 'adn@a', 'ROLE_ADMIN');

-- At the end of your schema script
CALL sp_register_user('adn@a', 'adn@a', 'adn@a');