CREATE TABLE watchlists
(
    id    UUID PRIMARY KEY,
    owner UUID         NOT NULL REFERENCES account (id),
    title VARCHAR(255) NOT NULL
);

CREATE TABLE watchlist_items
(
    watchlist_id       UUID NOT NULL REFERENCES watchlists (id),
    bookshelf_entry_id UUID NOT NULL REFERENCES bookshelf_entry (id),
    PRIMARY KEY (watchlist_id, bookshelf_entry_id)
);
