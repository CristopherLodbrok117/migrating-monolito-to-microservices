CREATE TABLE IF NOT EXISTS sinaloa_db.groups (
	id BIGINT UNSIGNED auto_increment NOT NULL,
	name VARCHAR(32) NOT NULL,
	created_at TIMESTAMP NOT NULL,
	updated_at TIMESTAMP NOT NULL,
	creator_id BIGINT UNSIGNED NOT NULL,
	CONSTRAINT groups_pk PRIMARY KEY (id),
	FOREIGN KEY (creator_id) REFERENCES users(id)
);