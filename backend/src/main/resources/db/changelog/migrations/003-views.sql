

CREATE OR REPLACE VIEW v_user_access_summary AS
SELECT
    up.user_id AS actor_id,
    u.id AS target_id,
    u.username AS target_username,

    EXISTS (
        SELECT 1 FROM user_permissions up2
        WHERE up2.user_id = up.user_id
          AND up2.target_id = u.id
          AND up2.permission_name != 'view_user'
    ) AS has_more_details
FROM app_user u
         INNER JOIN user_permissions up ON up.target_id = u.id
WHERE up.permission_name = 'view_user';

CREATE OR REPLACE VIEW v_user_global_capabilities AS
SELECT
    u.id AS user_id,
    EXISTS (
        SELECT 1 FROM user_permissions up
        WHERE up.user_id = u.id
          AND up.permission_name = 'view_user'
    ) AS can_view_user_list
FROM app_user u;