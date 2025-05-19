ALTER TABLE sinaloa_db.events
ADD COLUMN priority SMALLINT UNSIGNED NOT NULL DEFAULT 0;

ALTER TABLE sinaloa_db.events
ADD COLUMN category_id BIGINT UNSIGNED;

ALTER TABLE sinaloa_db.events
ADD CONSTRAINT events_categories_fk FOREIGN KEY (category_id) REFERENCES `event_categories` (id)
ON DELETE CASCADE;