CREATE TABLE IF NOT EXISTS sinaloa_db.events (
	id BIGINT UNSIGNED auto_increment NOT NULL,
	title VARCHAR(50) NOT NULL,
	notes VARCHAR(255),
	start_date TIMESTAMP NOT NULL,
	end_date TIMESTAMP NOT NULL,
	created_at TIMESTAMP NOT NULL,
	updated_at TIMESTAMP NOT NULL,
	creator_id BIGINT UNSIGNED,
	group_id BIGINT UNSIGNED,
	CONSTRAINT events_pk PRIMARY KEY (id),
	FOREIGN KEY (creator_id) REFERENCES users(id),
	FOREIGN KEY (group_id) REFERENCES `groups` (id)
);