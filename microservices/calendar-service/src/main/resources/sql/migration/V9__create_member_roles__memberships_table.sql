CREATE TABLE IF NOT EXISTS sinaloa_db.member_roles_memberships (
	membership_id BIGINT UNSIGNED NOT NULL,
	member_role_id BIGINT UNSIGNED NOT NULL,
	CONSTRAINT member_roles_memberships_pk PRIMARY KEY (membership_id, member_role_id),
	CONSTRAINT member_roles_memberships_pk_memberships_fk FOREIGN KEY (membership_id) REFERENCES memberships (id)
	    ON DELETE CASCADE,
	CONSTRAINT member_roles_memberships_pk_member_roles_fk FOREIGN KEY (member_role_id) REFERENCES `member_roles` (id)
	    ON DELETE CASCADE
);