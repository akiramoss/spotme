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

CREATE TABLE spot_images (
    spot_id BIGINT NOT NULL,
    image_order INT NOT NULL,
    image_url VARCHAR(2048) NOT NULL,
    CONSTRAINT fk_spot_images_spot FOREIGN KEY (spot_id) REFERENCES spots(id),
    PRIMARY KEY (spot_id, image_order)
);
