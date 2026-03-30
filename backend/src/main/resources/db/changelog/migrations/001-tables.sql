--liquibase formatted sql
--changeset 001-tables

CREATE TABLE app_user (
                          id UUID PRIMARY KEY,
                          username VARCHAR(50) NOT NULL UNIQUE,
                          email VARCHAR(100) NOT NULL UNIQUE,
                          password VARCHAR(255) NOT NULL,
                          role VARCHAR(20) NOT NULL
);
CREATE TABLE user_token (
                            id         UUID PRIMARY KEY,
                            owner_id   UUID NOT NULL,
                            message    TEXT, -- Changed from VARCHAR(200) to TEXT to fit JWTs
                            expiration TIMESTAMP NOT NULL,
                            CONSTRAINT fk_token_user FOREIGN KEY (owner_id) REFERENCES app_user(id)
);

CREATE TABLE api_log (
                         id          UUID PRIMARY KEY,
                         user_id     TEXT, -- Changed to TEXT for flexibility
                         action      TEXT, -- Changed to TEXT to avoid truncation on long log messages
                         status      TEXT,
                         duration_ms BIGINT,
                         name        VARCHAR(255),
                         created_at  TIMESTAMP NOT NULL
);

CREATE TABLE ref_room_type (
                               code VARCHAR(10) PRIMARY KEY,
                               description TEXT NOT NULL
);


CREATE TABLE app_room (
                          id UUID PRIMARY KEY,
                          room_number VARCHAR(10) NOT NULL,
                          type_code VARCHAR(10) REFERENCES ref_room_type(code)
);

-- 4. User Role (Requirement: Composite Primary Key)
CREATE TABLE user_role (
                           user_id UUID REFERENCES app_user(id),
                           role_name VARCHAR(50),
                           PRIMARY KEY (user_id, role_name) -- Composite Key
);

-- 5. User Profile
CREATE TABLE user_profile (
                              user_id UUID PRIMARY KEY REFERENCES app_user(id),
                              full_name VARCHAR(100),
                              phone_number VARCHAR(20)
);

-- 6. Reservation
CREATE TABLE app_reservation (
                                 id UUID PRIMARY KEY,
                                 user_id UUID REFERENCES app_user(id),
                                 room_id UUID REFERENCES app_room(id),
                                 start_date DATE NOT NULL,
                                 end_date DATE NOT NULL
);


CREATE TABLE room_amenity (
                              room_id UUID PRIMARY KEY REFERENCES app_room(id),
                              details JSONB -- e.g., {"wifi": true, "balcony": true}
);


CREATE TABLE audit_log (
                           id SERIAL PRIMARY KEY,
                           changed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                           operation VARCHAR(10),
                           content JSONB
);


CREATE TABLE app_invoice (
                             id UUID PRIMARY KEY,
                             reservation_id UUID REFERENCES app_reservation(id),
                             amount DECIMAL(10, 2),
                             is_paid BOOLEAN DEFAULT FALSE
);

-- 10. Revoked Token (Requirement: Index on Non-Key Column)
CREATE TABLE blacklisted_token (
                               token_id UUID PRIMARY KEY,
                               expiry TIMESTAMP NOT NULL
);


CREATE INDEX idx_token_expiry ON blacklisted_token(expiry);