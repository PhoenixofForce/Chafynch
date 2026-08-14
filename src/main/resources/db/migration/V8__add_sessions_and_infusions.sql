CREATE TABLE session (
	id BIGSERIAL PRIMARY KEY,
	tea_id BIGINT NOT NULL REFERENCES tea(id) ON DELETE CASCADE,
	start_time TIMESTAMP with time zone not null,
	last_updated TIMESTAMP with time zone not null,
	weight NUMERIC(10, 2),
	volume NUMERIC(10, 2),
	location VARCHAR,
	people VARCHAR,
	rating INTEGER CHECK (rating BETWEEN 0 AND 5),
	session_summary VARCHAR,
	next_session_hint VARCHAR
);
CREATE INDEX idx_session_tea ON session (tea_id);

DROP TABLE IF EXISTS session_tasting_notes;
CREATE TABLE session_tasting_notes (
	id BIGSERIAL PRIMARY KEY,
	session_id BIGINT NOT NULL REFERENCES session(id) ON DELETE CASCADE,
	tasting_note_id BIGINT NOT NULL REFERENCES tasting_note(id) ON DELETE CASCADE,
	category VARCHAR(255),
	sub_category VARCHAR(255)
);
ALTER TABLE session_tasting_notes ADD CONSTRAINT uq_session_tasting_notes UNIQUE (session_id, tasting_note_id, category, sub_category);
CREATE INDEX idx_session_tasting_notes_session ON session_tasting_notes (session_id);
CREATE INDEX idx_session_tasting_notes_tasting_note ON session_tasting_notes (tasting_note_id);

CREATE TABLE infusion (
	id BIGSERIAL PRIMARY KEY,
	session_id BIGINT NOT NULL REFERENCES session(id) ON DELETE CASCADE,
	start_time TIMESTAMP with time zone not null,
	infusion_time NUMERIC(10, 2),
	temperature NUMERIC(10, 2),
	rating INTEGER CHECK (rating BETWEEN 0 AND 5),
	is_rinse BOOLEAN NOT NULL default false
);
CREATE INDEX idx_infusion_session ON infusion (session_id);

CREATE TABLE infusion_tasting_notes (
	id BIGSERIAL PRIMARY KEY,
	infusion_id BIGINT NOT NULL REFERENCES infusion(id) ON DELETE CASCADE,
	tasting_note_id BIGINT NOT NULL REFERENCES tasting_note(id) ON DELETE CASCADE,
	category VARCHAR(255),
	sub_category VARCHAR(255)
);
ALTER TABLE infusion_tasting_notes ADD CONSTRAINT uq_infusion_tasting_notes UNIQUE (infusion_id, tasting_note_id, category, sub_category);
CREATE INDEX idx_infusion_tasting_notes_infusion ON infusion_tasting_notes (infusion_id);
CREATE INDEX idx_infusion_tasting_notes_tasting_note ON infusion_tasting_notes (tasting_note_id);
