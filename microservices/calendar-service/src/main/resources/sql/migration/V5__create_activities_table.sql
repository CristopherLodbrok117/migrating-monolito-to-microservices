CREATE TABLE IF NOT EXISTS sinaloa_db.activities (
	id BIGINT UNSIGNED auto_increment NOT NULL,
	name VARCHAR(50) NOT NULL,
	status SMALLINT NOT NULL DEFAULT 0,
	event_id BIGINT UNSIGNED NOT NULL,
	CONSTRAINT activities_pk PRIMARY KEY (id),
	FOREIGN KEY (event_id) REFERENCES events(id)
);