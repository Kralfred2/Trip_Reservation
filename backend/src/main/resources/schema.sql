
CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE IF NOT EXISTS app_role (
                                     id SERIAL PRIMARY KEY,
                                     name VARCHAR(50) UNIQUE NOT NULL
    );

-- 2. Create Permissions (Independent)
CREATE TABLE IF NOT EXISTS app_permission (
                                           id SERIAL PRIMARY KEY,
                                           code VARCHAR(50) UNIQUE NOT NULL,
    description TEXT
    );

-- 3. Create Users NOW (it can now find the 'roles' table)
CREATE TABLE IF NOT EXISTS app_user (
                                        id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

-- 4. Create everything else that depends on Users
CREATE TABLE IF NOT EXISTS app_user_permission (
                                                user_id INT REFERENCES users(id) ON DELETE CASCADE,
    permission_id INT REFERENCES permissions(id) ON DELETE CASCADE,
    PRIMARY KEY (user_id, permission_id)
    );

CREATE TABLE IF NOT EXISTS app_jwt_token (
                                          id SERIAL PRIMARY KEY,
                                          token_value TEXT UNIQUE NOT NULL,
                                          user_id INT REFERENCES users(id) ON DELETE CASCADE,
    is_revoked BOOLEAN DEFAULT FALSE,
    expiry_date TIMESTAMP NOT NULL
    );