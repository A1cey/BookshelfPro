CREATE TABLE account
(
    id       UUID PRIMARY KEY,
    username VARCHAR(30)  NOT NULL UNIQUE,
    email    VARCHAR(255),
    password VARCHAR(255) NOT NULL
);

CREATE TABLE media_item
(
    id              UUID PRIMARY KEY,
    type            VARCHAR(50)  NOT NULL CHECK (type in ('BOOK', 'MOVIE', 'SERIES', 'MUSIC')),
    title           VARCHAR(255) NOT NULL,
    subtitle        VARCHAR(255),
    description     TEXT,
    cover_image_url TEXT,
    owner_id        UUID         NOT NULL REFERENCES account (id)
);

CREATE TABLE media_item_language
(
    media_item_id UUID       NOT NULL REFERENCES media_item (id),
    iso_code      VARCHAR(3) NOT NULL,
    PRIMARY KEY (media_item_id, iso_code)
);

CREATE TABLE book
(
    id            UUID PRIMARY KEY REFERENCES media_item (id),
    isbn          VARCHAR(13),
    page_count    INT NOT NULL,
    publish_date  DATE,
    publisher     VARCHAR(255),
    publish_place VARCHAR(255)
);

CREATE TABLE book_author
(
    book_id UUID         NOT NULL REFERENCES book (id),
    name    VARCHAR(255) NOT NULL,
    PRIMARY KEY (book_id, name)
);