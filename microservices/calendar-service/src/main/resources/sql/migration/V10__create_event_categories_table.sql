CREATE TABLE IF NOT EXISTS sinaloa_db.event_categories (
	id BIGINT UNSIGNED auto_increment NOT NULL,
	name VARCHAR(50) NOT NULL,
	group_id BIGINT UNSIGNED NOT NULL,
	CONSTRAINT activities_pk PRIMARY KEY (id),
	FOREIGN KEY (group_id) REFERENCES `groups`(id)
);