ALTER TABLE tea
ADD COLUMN description_md VARCHAR,
ADD COLUMN harvest_year INTEGER,
ADD COLUMN harvest_label VARCHAR(50),
ADD COLUMN website VARCHAR(500),
ADD COLUMN rating INTEGER CHECK (rating BETWEEN 0 AND 5);

CREATE UNIQUE INDEX uq_tea_name_vendor_harvest
    ON tea (name, COALESCE(vendor_id, 0), COALESCE(harvest_year, 0));

CREATE TABLE tasting_note (
   id BIGSERIAL PRIMARY KEY,
   note VARCHAR(255) NOT NULL unique
);

CREATE TABLE session_tasting_notes (
  tea_id BIGINT NOT NULL REFERENCES tea(id) ON DELETE CASCADE,
  tasting_note_id BIGINT NOT NULL REFERENCES tasting_note(id) ON DELETE CASCADE,
  PRIMARY KEY (tea_id, tasting_note_id)
);