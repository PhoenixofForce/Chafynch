CREATE TABLE extraction_profile (
      id BIGSERIAL PRIMARY KEY,
      name VARCHAR(255) NOT NULL unique,
      valid_urls VARCHAR
);

CREATE TABLE extraction_setting (
    id BIGSERIAL PRIMARY KEY,
    profile_id BIGINT NOT NULL REFERENCES extraction_profile(id) ON DELETE CASCADE,
    field VARCHAR(255) NOT NULL,
    selector VARCHAR,
    regex VARCHAR,
    operations VARCHAR,
    grab_all BOOLEAN NOT NULL
);

CREATE UNIQUE INDEX uq_profile_id_field
    ON extraction_setting(profile_id, field);