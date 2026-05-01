-- Core User Tables
CREATE TABLE IF NOT EXISTS app_user (
                                        id UUID PRIMARY KEY,
                                        username VARCHAR(50) NOT NULL UNIQUE,
                                        email VARCHAR(100) NOT NULL UNIQUE,
                                        password VARCHAR(255) NOT NULL,
                                        role VARCHAR(20) NOT NULL
);



CREATE TABLE IF NOT EXISTS user_permissions (
                                  id UUID PRIMARY KEY,
                                  user_id UUID NOT NULL,
                                  permission_name VARCHAR(100) NOT NULL, -- e.g., 'email', 'username', 'activate'
                                  access_level VARCHAR(20) NOT NULL,    -- e.g., 'VIEW', 'MODIFY'
                                  target_id UUID,                       -- NULL for global, or ID of a specific entity
    -- Ensures a user doesn't have duplicate permission entries for the same target
                                  CONSTRAINT unique_user_perm_target UNIQUE (user_id, permission_name, target_id),

    -- Foreign key to your app_user table
                                  CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES app_user(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS user_token (
                                          id UUID PRIMARY KEY,
                                          owner_id UUID NOT NULL,
                                          message TEXT,
                                          expiration TIMESTAMP NOT NULL,
                                          CONSTRAINT fk_token_user FOREIGN KEY (owner_id) REFERENCES app_user(id)
);

-- Infrastructure & Logging
CREATE TABLE IF NOT EXISTS api_log (
                                       id UUID PRIMARY KEY,
                                       user_id TEXT,
                                       action TEXT,
                                       status TEXT,
                                       duration_ms BIGINT,
                                       name VARCHAR(255),
                                       created_at TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS audit_log (
                                         id SERIAL PRIMARY KEY,
                                         changed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                         operation VARCHAR(10),
                                         content JSONB
);




CREATE TABLE IF NOT EXISTS user_role (
                                         user_id UUID REFERENCES app_user(id),
                                         role_name VARCHAR(50),
                                         PRIMARY KEY (user_id, role_name)
);

CREATE TABLE IF NOT EXISTS user_profile (
                                            user_id UUID PRIMARY KEY REFERENCES app_user(id),
                                            full_name VARCHAR(100),
                                            phone_number VARCHAR(20)
);



-- Security
CREATE TABLE IF NOT EXISTS blacklisted_token (
                                                 token_id UUID PRIMARY KEY,
                                                 expiry TIMESTAMP NOT NULL
);

CREATE INDEX IF NOT EXISTS idx_token_expiry ON blacklisted_token(expiry);
