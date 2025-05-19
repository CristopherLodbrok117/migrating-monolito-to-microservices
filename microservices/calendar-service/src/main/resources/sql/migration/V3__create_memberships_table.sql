CREATE TABLE IF NOT EXISTS sinaloa_db.memberships (
	id BIGINT UNSIGNED auto_increment NOT NULL,
	user_id BIGINT UNSIGNED NOT NULL,
	group_id BIGINT UNSIGNED NOT NULL,
	created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
	CONSTRAINT memberships_pk PRIMARY KEY (id),
	CONSTRAINT memberships_users_fk FOREIGN KEY (user_id) REFERENCES users (id)
	    ON DELETE CASCADE,
	CONSTRAINT memberships_groups_fk FOREIGN KEY (group_id) REFERENCES `groups` (id)
	    ON DELETE CASCADE
);