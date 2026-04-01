CREATE TABLE bookshelf_entry
(
    id            UUID PRIMARY KEY,
    media_item_id UUID NOT NULL REFERENCES media_item (id),
    owner         UUID NOT NULL REFERENCES account (id),
    UNIQUE (owner, media_item_id)
);

CREATE TABLE bookshelf_entry_label
(
    bookshelf_entry_id UUID         NOT NULL REFERENCES bookshelf_entry (id),
    label              VARCHAR(255) NOT NULL,
    PRIMARY KEY (bookshelf_entry_id, label)
);


CREATE TABLE consumption_progress
(
    id                       UUID PRIMARY KEY,
    bookshelf_entry_id       UUID        NOT NULL REFERENCES bookshelf_entry (id),
    state                    VARCHAR(11) NOT NULL CHECK (state in ('NOT_STARTED', 'STARTED', 'COMPLETED')),
    type                     VARCHAR(50) NOT NULL CHECK (type IN ('BOOK', 'MOVIE', 'EPISODE', 'MUSIC')),

    -- BOOK: pages
    current_page             INT,
    total_pages              INT,

    -- MOVIE/EPISODE/MUSIC: duration in seconds
    current_duration_seconds INT,
    total_duration_seconds   INT,

    -- Constraint: book fields must be present for books
    CONSTRAINT cp_chk_book_progress CHECK (
        type != 'BOOK'
        OR (
            current_page IS NOT NULL
            AND total_pages IS NOT NULL
            AND current_page >= 0
            AND current_page <= total_pages
            AND current_duration_seconds IS NULL
            AND total_duration_seconds IS NULL
        )
) ,
    CONSTRAINT cp_chk_movie_episode_music_progress CHECK (
        type NOT IN ('MOVIE', 'EPISODE', 'MUSIC')
        OR (
            current_duration_seconds IS NOT NULL
            AND total_duration_seconds IS NOT NULL
            AND current_duration_seconds >= 0
            AND current_duration_seconds <= total_duration_seconds
            AND current_page IS NULL
            AND total_pages IS NULL
        )
    )
);

CREATE TABLE consumption_progress_snapshot
(
    id                       UUID PRIMARY KEY,
    consumption_state        VARCHAR(11) NOT NULL CHECK (consumption_state in ('NOT_STARTED', 'STARTED', 'COMPLETED')),
    type                     VARCHAR(50) NOT NULL CHECK (type IN ('BOOK', 'MOVIE', 'EPISODE', 'MUSIC')),
    created_at               TIMESTAMP   NOT NULL DEFAULT now(),

    -- Book: pages
    current_page             INT,
    total_pages              INT,

    -- MOVIE/EPISODE/MUSIC
    current_duration_seconds INT,
    total_duration_seconds   INT,

    -- Constraint: book fields must be present for books
    CONSTRAINT cps_chk_book_progress CHECK (
        type != 'BOOK'
        OR (
            current_page IS NOT NULL
            AND total_pages IS NOT NULL
            AND current_page >= 0
            AND current_page <= total_pages
            AND current_duration_seconds IS NULL
            AND total_duration_seconds IS NULL
        )
) ,
    CONSTRAINT cps_chk_movie_episode_music_progress CHECK (
        type NOT IN ('MOVIE', 'EPISODE', 'MUSIC')
        OR (
            current_duration_seconds IS NOT NULL
            AND total_duration_seconds IS NOT NULL
            AND current_duration_seconds >= 0
            AND current_duration_seconds <= total_duration_seconds
            AND current_page IS NULL
            AND total_pages IS NULL
        )
    )
);

CREATE TABLE review
(
    id            UUID PRIMARY KEY,
    media_item_id UUID NOT NULL REFERENCES media_item (id),
    owner         UUID NOT NULL REFERENCES account (id),
    UNIQUE (owner, media_item_id)
);

CREATE TABLE review_change
(
    id                               UUID PRIMARY KEY,
    review_id                        UUID      NOT NULL REFERENCES review (id),
    rating                           FLOAT     NOT NULL check ( rating BETWEEN 0.0 AND 10.0),
    comment                          TEXT      NOT NULL,
    review_date                      TIMESTAMP NOT NULL,
    consumption_progress_snapshot_id UUID      NOT NULL REFERENCES consumption_progress_snapshot (id)
);
