CREATE TABLE IF NOT EXISTS sinaloa_db.users (
	id BIGINT UNSIGNED auto_increment NOT NULL,
	email VARCHAR(50) NOT NULL,
	password VARCHAR(100) NOT NULL,
	username VARCHAR(32) NOT NULL,
	created_at TIMESTAMP NOT NULL,
	updated_at TIMESTAMP NOT NULL,
	deleted_at TIMESTAMP NULL,
	CONSTRAINT users_pk PRIMARY KEY (id),
	CONSTRAINT users_unique UNIQUE KEY (email),
	CONSTRAINT users_unique_1 UNIQUE KEY (username)
);