-- Creates the users table.
-- Each row represents a registered SpotMe account.
-- email is unique and used as the login identifier (Beta 3: registration/login).
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);