ALTER TABLE review_change
    DROP CONSTRAINT review_change_pkey,
    DROP CONSTRAINT review_change_consumption_progress_snapshot_id_fkey,
    DROP COLUMN consumption_progress_snapshot_id,
    ADD COLUMN consumption_progress_snapshot_consumption_progress_id UUID,
    ADD COLUMN consumption_progress_snapshot_created_at              TIMESTAMP;

ALTER TABLE consumption_progress_snapshot
    ADD COLUMN consumption_progress_id UUID NOT NULL DEFAULT '00000000-0000-0000-0000-000000000000'::UUID REFERENCES consumption_progress (id),
    DROP CONSTRAINT consumption_progress_snapshot_pkey,
    DROP COLUMN id,
    ADD CONSTRAINT consumption_progress_snapshot_pkey PRIMARY KEY (consumption_progress_id, created_at);

ALTER TABLE review_change
    ADD CONSTRAINT review_change_consumption_progress_snapshot_fkey
        FOREIGN KEY (consumption_progress_snapshot_consumption_progress_id, consumption_progress_snapshot_created_at)
            REFERENCES consumption_progress_snapshot (consumption_progress_id, created_at);

ALTER TABLE review_change
    ALTER COLUMN consumption_progress_snapshot_consumption_progress_id SET NOT NULL,
    ALTER COLUMN consumption_progress_snapshot_created_at SET NOT NULL,
    ADD CONSTRAINT review_change_pkey PRIMARY KEY (review_id, review_date);