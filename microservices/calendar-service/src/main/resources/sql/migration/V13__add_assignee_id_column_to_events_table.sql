ALTER TABLE sinaloa_db.events
ADD COLUMN assignee_id BIGINT UNSIGNED DEFAULT NULL;

ALTER TABLE sinaloa_db.events
ADD CONSTRAINT events_assignee_fk FOREIGN KEY (assignee_id) REFERENCES `users` (id)
ON DELETE CASCADE;