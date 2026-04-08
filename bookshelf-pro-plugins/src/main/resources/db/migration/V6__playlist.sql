CREATE TABLE playlists
(
    id    UUID PRIMARY KEY,
    owner UUID         NOT NULL REFERENCES account (id),
    title VARCHAR(255) NOT NULL
);

CREATE TABLE playlist_items
(
    playlist_id        UUID NOT NULL REFERENCES playlists (id),
    bookshelf_entry_id UUID NOT NULL REFERENCES bookshelf_entry (id),
    position           INT  NOT NULL,
    PRIMARY KEY (playlist_id, bookshelf_entry_id),
    UNIQUE (position, playlist_id)
);
