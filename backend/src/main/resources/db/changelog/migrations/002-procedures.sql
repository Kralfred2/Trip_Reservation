CREATE OR REPLACE PROCEDURE sp_register_user(
    p_username VARCHAR,
    p_password VARCHAR,
    p_email VARCHAR,
    OUT p_user_id UUID
)
LANGUAGE plpgsql
AS $$
BEGIN

    IF EXISTS (SELECT 1 FROM app_user WHERE email = p_email OR username = p_username) THEN
        RAISE EXCEPTION 'User already exists with this email or username';
END IF;


    p_user_id := gen_random_uuid();
INSERT INTO app_user (id, username, password, email)
VALUES (p_user_id, p_username, p_password, p_email);


INSERT INTO user_roles (user_id, role_name)
VALUES (p_user_id, 'ROLE_USER');



EXCEPTION WHEN OTHERS THEN

    p_user_id := NULL;
ROLLBACK;
RAISE NOTICE 'Registration failed, rolling back changes.';
END;
$$;


CREATE OR REPLACE PROCEDURE sp_global_logout(
    p_user_id UUID,
    OUT p_count_revoked INTEGER
)
LANGUAGE plpgsql
AS $$
BEGIN

SELECT COUNT(*) INTO p_count_revoked FROM user_tokens WHERE user_id = p_user_id;


DELETE FROM user_tokens WHERE user_id = p_user_id;


EXCEPTION WHEN OTHERS THEN
    ROLLBACK;
    p_count_revoked := 0;
    RAISE NOTICE 'Failed to log out all devices for user %', p_user_id;
END;
$$;