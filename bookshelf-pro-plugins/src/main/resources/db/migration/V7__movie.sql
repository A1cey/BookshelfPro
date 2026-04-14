CREATE TABLE movie
(
    id             UUID PRIMARY KEY REFERENCES media_item (id),
    imdb_title_id  VARCHAR(50) UNIQUE NOT NULL,
    duration       INT                NOT NULL,
    release_date   DATE,
    origin_country VARCHAR(255)       NOT NULL
);

CREATE TABLE movie_actor
(
    movie_id UUID         NOT NULL references movie (id),
    name     VARCHAR(255) NOT NULL,
    role     VARCHAR(255) NOT NULL,
    PRIMARY KEY (movie_id, name, role)
);

CREATE TABLE movie_director
(
    movie_id UUID         NOT NULL references movie (id),
    name     VARCHAR(255) NOT NULL,
    PRIMARY KEY (movie_id, name)
);

CREATE TABLE movie_studio
(
    movie_id UUID         NOT NULL references movie (id),
    name     VARCHAR(255) NOT NULL,
    PRIMARY KEY (movie_id, name)
);

