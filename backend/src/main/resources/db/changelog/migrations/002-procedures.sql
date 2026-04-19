CREATE OR REPLACE PROCEDURE sp_register_user(
    p_username VARCHAR,
    p_email VARCHAR,
    p_password VARCHAR
)
    LANGUAGE plpgsql
AS $$
DECLARE
    v_user_id UUID;
BEGIN
    -- 1. Insert the new user and capture the generated ID
    INSERT INTO app_user (id, username, email, password, role)
    VALUES (gen_random_uuid(), p_username, p_email, p_password, 'GUEST')
    RETURNING id INTO v_user_id;

    -- 2. Insert default permissions for the new user
    INSERT INTO user_permissions (user_id, permission)
    VALUES
        (v_user_id, 'read_own_profile'),
        (v_user_id, 'access_basic_features');
END;
$$;


CREATE OR REPLACE PROCEDURE sp_global_logout(
    p_user_id UUID,
    OUT p_count_revoked INTEGER
)
    LANGUAGE plpgsql
AS $$
BEGIN
    -- Count how many tokens exist for this owner
    SELECT COUNT(*) INTO p_count_revoked
    FROM user_token
    WHERE owner_id = p_user_id;

    -- Delete the tokens
    DELETE FROM user_token WHERE owner_id = p_user_id;

EXCEPTION WHEN OTHERS THEN
    p_count_revoked := 0;
    RAISE NOTICE 'Failed to log out all devices for user %', p_user_id;
-- In procedures, the transaction will automatically abort on unhandled exceptions.
END;
$$;