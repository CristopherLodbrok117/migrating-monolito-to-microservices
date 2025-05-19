CREATE TABLE IF NOT EXISTS sinaloa_db.users_system_roles (
	id BIGINT UNSIGNED auto_increment NOT NULL,
	user_id BIGINT UNSIGNED NOT NULL,
	system_role_id BIGINT UNSIGNED NOT NULL,
	CONSTRAINT users_system_roles_pk PRIMARY KEY (id),
	CONSTRAINT users_system_roles_users_fk FOREIGN KEY (user_id) REFERENCES users (id)
	    ON DELETE CASCADE,
	CONSTRAINT users_system_roles_system_roles_fk FOREIGN KEY (system_role_id) REFERENCES `system_roles` (id)
	    ON DELETE CASCADE
);