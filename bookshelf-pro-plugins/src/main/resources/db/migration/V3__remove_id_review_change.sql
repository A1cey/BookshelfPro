ALTER TABLE review_change
    DROP CONSTRAINT review_change_pkey;

ALTER TABLE review_change
    DROP COLUMN id;

ALTER TABLE review_change
    ADD CONSTRAINT review_change_pkey
        PRIMARY KEY (review_id, review_date, consumption_progress_snapshot_id);