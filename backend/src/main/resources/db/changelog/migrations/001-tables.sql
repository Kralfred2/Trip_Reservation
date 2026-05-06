
CREATE TABLE IF NOT EXISTS app_user (
                          id UUID PRIMARY KEY,
                          username VARCHAR(50) NOT NULL UNIQUE,
                          email VARCHAR(100) NOT NULL UNIQUE,
                          password VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS user_role (
                           user_id UUID REFERENCES app_user(id) ON DELETE CASCADE,
                           role_name VARCHAR(50),
                           PRIMARY KEY (user_id, role_name)
);

CREATE TABLE IF NOT EXISTS user_target_permissions (
                                         id UUID PRIMARY KEY,
                                         user_id UUID NOT NULL REFERENCES app_user(id) ON DELETE CASCADE,
                                         permission_name VARCHAR(100) NOT NULL,
                                         target_id UUID, -- Can be NULL for global permissions
                                         CONSTRAINT unique_user_perm_target UNIQUE (user_id, permission_name, target_id)
);

CREATE TABLE IF NOT EXISTS user_group (
                            id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                            name VARCHAR(100) UNIQUE NOT NULL,
                            parent_id UUID REFERENCES user_group(id) ON DELETE CASCADE,
                            description TEXT,
                            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS group_roles (
                             id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                             group_id UUID REFERENCES user_group(id) ON DELETE CASCADE,
                             name VARCHAR(50) NOT NULL,
                             description TEXT,
                             UNIQUE (group_id, name)
);

CREATE TABLE IF NOT EXISTS group_members (
                               group_id UUID REFERENCES user_group(id) ON DELETE CASCADE,
                               user_id UUID REFERENCES app_user(id) ON DELETE CASCADE,
                               group_role_id UUID REFERENCES group_roles(id),
                               PRIMARY KEY (group_id, user_id)
);

CREATE TABLE IF NOT EXISTS api_log (
                         id UUID PRIMARY KEY,
                         user_id TEXT,
                         action TEXT,
                         status TEXT,
                         duration_ms BIGINT,
                         name VARCHAR(255),
                         created_at TIMESTAMP NOT NULL
);