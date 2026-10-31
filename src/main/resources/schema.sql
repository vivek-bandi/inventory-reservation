CREATE TABLE inventory(
    product_id BIGINT PRIMARY KEY,
    available_stock INT NOT NULL CHECK (available_stock >= 0)
);

CREATE TABLE reservation(
    reservation_id BIGSERIAL PRIMARY KEY,
    product_id BIGINT NOT NULL REFERENCES inventory(product_id),
    quantity INT NOT NULL CHECK(quantity > 0)
);