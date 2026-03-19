CREATE TABLE location (
    id       BIGSERIAL    PRIMARY KEY,
    country  VARCHAR(255) NOT NULL,
    province VARCHAR(255),
    city     VARCHAR(255)
);

CREATE UNIQUE INDEX uq_location_country_province_city
    ON location (country, COALESCE(province, ''), COALESCE(city, ''));

CREATE TABLE cultivar (
    id   BIGSERIAL    PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE tea_type (
    id   BIGSERIAL    PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE vendor (
    id          BIGSERIAL    PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    website     VARCHAR(500),
    location_id BIGINT       REFERENCES location(id)
);

CREATE INDEX idx_vendor_location ON vendor (location_id);

CREATE TABLE tea (
    id                 BIGSERIAL      PRIMARY KEY,
    name               VARCHAR(255)   NOT NULL,
    cultivar_id        BIGINT         REFERENCES cultivar(id),
    tea_type_id        BIGINT         REFERENCES tea_type(id),
    vendor_id          BIGINT         REFERENCES vendor(id),
    origin_location_id BIGINT         REFERENCES location(id),
    price              NUMERIC(10, 2),
    purchase_date      DATE,
    weight_grams       NUMERIC(10, 2)
);

CREATE INDEX idx_tea_cultivar ON tea (cultivar_id);
CREATE INDEX idx_tea_type     ON tea (tea_type_id);
CREATE INDEX idx_tea_vendor   ON tea (vendor_id);
CREATE INDEX idx_tea_origin   ON tea (origin_location_id);