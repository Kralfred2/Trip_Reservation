-- Core User Tables
CREATE TABLE IF NOT EXISTS app_user (
                                        id UUID PRIMARY KEY,
                                        username VARCHAR(50) NOT NULL UNIQUE,
                                        email VARCHAR(100) NOT NULL UNIQUE,
                                        password VARCHAR(255) NOT NULL,
                                        role VARCHAR(20) NOT NULL
);

CREATE TABLE IF NOT EXISTS user_permissions (
                                                user_id UUID NOT NULL,
                                                permission VARCHAR(255) NOT NULL,
                                                CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES app_user(id) ON DELETE CASCADE,
                                                PRIMARY KEY (user_id, permission)
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

-- Domain Specific Tables (Rooms & Roles)
CREATE TABLE IF NOT EXISTS ref_room_type (
                                             code VARCHAR(10) PRIMARY KEY,
                                             description TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS app_room (
                                        id UUID PRIMARY KEY,
                                        room_number VARCHAR(10) NOT NULL,
                                        type_code VARCHAR(10) REFERENCES ref_room_type(code)
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

-- Reservations & Finance
CREATE TABLE IF NOT EXISTS app_reservation (
                                               id UUID PRIMARY KEY,
                                               user_id UUID REFERENCES app_user(id),
                                               room_id UUID REFERENCES app_room(id),
                                               start_date DATE NOT NULL,
                                               end_date DATE NOT NULL
);

CREATE TABLE IF NOT EXISTS room_amenity (
                                            room_id UUID PRIMARY KEY REFERENCES app_room(id),
                                            details JSONB
);

CREATE TABLE IF NOT EXISTS app_invoice (
                                           id UUID PRIMARY KEY,
                                           reservation_id UUID REFERENCES app_reservation(id),
                                           amount DECIMAL(10, 2),
                                           is_paid BOOLEAN DEFAULT FALSE
);

-- Security
CREATE TABLE IF NOT EXISTS blacklisted_token (
                                                 token_id UUID PRIMARY KEY,
                                                 expiry TIMESTAMP NOT NULL
);

CREATE INDEX IF NOT EXISTS idx_token_expiry ON blacklisted_token(expiry);