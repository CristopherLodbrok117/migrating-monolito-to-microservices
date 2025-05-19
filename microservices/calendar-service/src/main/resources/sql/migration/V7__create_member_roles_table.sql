CREATE TABLE IF NOT EXISTS sinaloa_db.member_roles (
	id BIGINT UNSIGNED auto_increment NOT NULL,
	name VARCHAR(50) NOT NULL,
	CONSTRAINT member_roles_pk PRIMARY KEY (id),
	CONSTRAINT member_roles_roles_name_unique UNIQUE KEY (name)
);