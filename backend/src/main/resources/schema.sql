-- 1. Create Roles FIRST (because Users needs it)
CREATE TABLE IF NOT EXISTS roles (
                                     id SERIAL PRIMARY KEY,
                                     name VARCHAR(50) UNIQUE NOT NULL
    );

-- 2. Create Permissions (Independent)
CREATE TABLE IF NOT EXISTS permissions (
                                           id SERIAL PRIMARY KEY,
                                           code VARCHAR(50) UNIQUE NOT NULL,
    description TEXT
    );

-- 3. Create Users NOW (it can now find the 'roles' table)
CREATE TABLE IF NOT EXISTS users (
                                     id SERIAL PRIMARY KEY,
                                     username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    role_id INT REFERENCES roles(id),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

-- 4. Create everything else that depends on Users
CREATE TABLE IF NOT EXISTS user_permissions (
                                                user_id INT REFERENCES users(id) ON DELETE CASCADE,
    permission_id INT REFERENCES permissions(id) ON DELETE CASCADE,
    PRIMARY KEY (user_id, permission_id)
    );

CREATE TABLE IF NOT EXISTS jwt_tokens (
                                          id SERIAL PRIMARY KEY,
                                          token_value TEXT UNIQUE NOT NULL,
                                          user_id INT REFERENCES users(id) ON DELETE CASCADE,
    is_revoked BOOLEAN DEFAULT FALSE,
    expiry_date TIMESTAMP NOT NULL
    );