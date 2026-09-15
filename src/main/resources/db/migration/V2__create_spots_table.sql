-- Creates the spots table: a user's personal geolocated bookmark.
-- category and visibility are stored as text (not integers) so that new
-- enum values can be added later without corrupting existing rows
-- (see Category.java / Visibility.java for the corresponding Java enums).
-- visibility is modeled now but has no active business logic until
-- Beta 6 (sharing/followers) — every spot behaves as PRIVATE today.
CREATE TABLE spots (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    latitude DOUBLE PRECISION NOT NULL,
    longitude DOUBLE PRECISION NOT NULL,
    category VARCHAR(50) NOT NULL,
    visibility VARCHAR(50) NOT NULL,
    user_id BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    CONSTRAINT fk_spots_user FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Backing table for Spot.imageUrls (@ElementCollection).
-- Composite primary key (spot_id, image_order) — no surrogate id needed,
-- since a row is uniquely identified by "which spot" + "which position".
-- Order is fixed at spot creation (see Spot's constructor validation, max 5 images).
CREATE TABLE spot_images (
    spot_id BIGINT NOT NULL,
    image_order INT NOT NULL,
    image_url VARCHAR(2048) NOT NULL,
    CONSTRAINT fk_spot_images_spot FOREIGN KEY (spot_id) REFERENCES spots(id),
    PRIMARY KEY (spot_id, image_order)
);