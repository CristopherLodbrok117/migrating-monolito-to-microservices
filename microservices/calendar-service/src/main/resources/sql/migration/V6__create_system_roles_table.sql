CREATE TABLE IF NOT EXISTS sinaloa_db.system_roles (
	id BIGINT UNSIGNED auto_increment NOT NULL,
	name VARCHAR(50) NOT NULL,
	CONSTRAINT system_roles_pk PRIMARY KEY (id),
	CONSTRAINT system_roles_name_unique UNIQUE KEY (name)
);