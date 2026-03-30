CREATE OR REPLACE PROCEDURE sp_register_user(
    p_username VARCHAR,
    p_email VARCHAR,
    p_password VARCHAR
)
LANGUAGE plpgsql
AS $$
BEGIN
    -- 1. Check if Email already exists
    IF EXISTS (SELECT 1 FROM app_user WHERE email = p_email) THEN
        RAISE EXCEPTION 'Registration Error: Email % is already taken', p_email;
END IF;

    -- 2. Check if Username already exists
    IF EXISTS (SELECT 1 FROM app_user WHERE username = p_username) THEN
        RAISE EXCEPTION 'Registration Error: Username % is already taken', p_username;
END IF;

    -- 3. Perform the Insert with the correct ROLE string
INSERT INTO app_user (id, username, email, password, role)
VALUES (gen_random_uuid(), p_username, p_email, p_password, 'ROLE_USER');

END;
$$;


CREATE OR REPLACE PROCEDURE sp_global_logout(
    p_user_id UUID,
    OUT p_count_revoked INTEGER
)
LANGUAGE plpgsql
AS $$
BEGIN

SELECT COUNT(*) INTO p_count_revoked FROM user_token WHERE user_id = p_user_id;


DELETE FROM user_token WHERE user_id = p_user_id;


EXCEPTION WHEN OTHERS THEN
    ROLLBACK;
    p_count_revoked := 0;
    RAISE NOTICE 'Failed to log out all devices for user %', p_user_id;
END;
$$;