-- Adds a unique display username, separate from email (which remains
-- the login identifier). Not used for authentication.
ALTER TABLE users
    ADD COLUMN username VARCHAR(45) NOT NULL UNIQUE;