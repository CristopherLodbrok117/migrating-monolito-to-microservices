CREATE TABLE IF NOT EXISTS sinaloa_db.invitation_tokens (
	id BIGINT UNSIGNED auto_increment NOT NULL,
	token VARCHAR(50) NOT NULL,
	membership_id BIGINT UNSIGNED NOT NULL,
	created_at TIMESTAMP NOT NULL,
	CONSTRAINT invitation_tokens_pk PRIMARY KEY (id),
	CONSTRAINT invitation_tokens_memberships_fk FOREIGN KEY (membership_id) REFERENCES memberships (id)
    	    ON DELETE CASCADE
);