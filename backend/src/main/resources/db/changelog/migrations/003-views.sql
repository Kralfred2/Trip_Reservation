
CREATE OR REPLACE VIEW v_user_access_summary AS
WITH distinct_permissions AS (
    -- Get a unique list of who can see whom
    SELECT DISTINCT user_id AS actor_id, target_id
    FROM user_target_permissions
)
SELECT
    dp.actor_id,
    u.id AS target_id,
    u.username AS target_username,
    -- Check if they have more than just a basic view right
    EXISTS (
        SELECT 1 FROM user_target_permissions utp
        WHERE utp.user_id = dp.actor_id
          AND utp.target_id = u.id
          AND utp.permission_name NOT IN ('view_user', 'VIEW')
    ) AS has_more_details
FROM app_user u
         JOIN distinct_permissions dp ON dp.target_id = u.id;


CREATE OR REPLACE VIEW v_nested_group_admin_view AS
SELECT
    ug.id,
    ug.name,
    ug.description,
    ug.parent_id,
    ug.created_by,
    (SELECT array_agg(user_id) FROM group_members WHERE group_id = ug.id) as member_ids,
    (SELECT array_agg(id) FROM group_roles WHERE group_ids = ug.id) as role_ids,
    (SELECT array_agg(permission_id) FROM group_admin_permissions WHERE group_id = ug.id) as admin_perms
FROM user_group ug;


CREATE OR REPLACE VIEW v_user_global_capabilities AS
SELECT
    u.id AS user_id,
    -- Returns true if the user is a ROLE_ADMIN OR has a global 'VIEW_USER_LIST' permission
    (
        EXISTS (SELECT 1 FROM user_role ur WHERE ur.user_id = u.id AND ur.role_name = 'ROLE_ADMIN')
            OR
        EXISTS (SELECT 1 FROM user_target_permissions utp
                WHERE utp.user_id = u.id
                  AND utp.permission_name = 'VIEW_USER_LIST'
                  AND utp.target_id IS NULL)
        ) AS can_view_user_list
FROM app_user u;

CREATE OR REPLACE VIEW v_active_permissions AS
SELECT
    gm.user_id,
    gm.group_id,
    p.name AS permission_name
FROM group_members gm
         JOIN group_roles gr ON gm.group_role_id = gr.id
         JOIN group_role_permissions grp ON gr.id = grp.group_role_id
         JOIN user_permissions p ON grp.permission_id = p.id;