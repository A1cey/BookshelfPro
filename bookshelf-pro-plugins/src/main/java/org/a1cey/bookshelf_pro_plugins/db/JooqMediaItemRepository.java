package org.a1cey.bookshelf_pro_plugins.db;

import java.net.URI;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.a1cey.bookshelf_pro_domain.Id;
import org.a1cey.bookshelf_pro_domain.Title;
import org.a1cey.bookshelf_pro_domain.account.AccountId;
import org.a1cey.bookshelf_pro_domain.media_item.Description;
import org.a1cey.bookshelf_pro_domain.media_item.Language;
import org.a1cey.bookshelf_pro_domain.media_item.MediaItem;
import org.a1cey.bookshelf_pro_domain.media_item.MediaItemId;
import org.a1cey.bookshelf_pro_domain.media_item.MediaItemRepository;
import org.a1cey.bookshelf_pro_domain.media_item.MediaItemSearchCriteria;
import org.a1cey.bookshelf_pro_domain.media_item.MediaItemType;
import org.a1cey.bookshelf_pro_domain.media_item.Subtitle;
import org.a1cey.bookshelf_pro_domain.media_item.book.Author;
import org.a1cey.bookshelf_pro_domain.media_item.book.Book;
import org.a1cey.bookshelf_pro_domain.media_item.book.Isbn;
import org.a1cey.bookshelf_pro_domain.media_item.book.PageCount;
import org.a1cey.bookshelf_pro_domain.media_item.book.PublishDate;
import org.a1cey.bookshelf_pro_domain.media_item.book.PublishPlace;
import org.a1cey.bookshelf_pro_domain.media_item.book.Publisher;
import org.a1cey.bookshelf_pro_domain.media_item.movie.Actor;
import org.a1cey.bookshelf_pro_domain.media_item.movie.Director;
import org.a1cey.bookshelf_pro_domain.media_item.movie.Duration;
import org.a1cey.bookshelf_pro_domain.media_item.movie.ImdbTitleId;
import org.a1cey.bookshelf_pro_domain.media_item.movie.Movie;
import org.a1cey.bookshelf_pro_domain.media_item.movie.OriginCountry;
import org.a1cey.bookshelf_pro_domain.media_item.movie.ReleaseDate;
import org.a1cey.bookshelf_pro_domain.media_item.movie.Studio;
import org.a1cey.bookshelf_pro_plugins.db.jooq.tables.records.MediaItemRecord;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import static org.a1cey.bookshelf_pro_plugins.db.jooq.Tables.BOOK;
import static org.a1cey.bookshelf_pro_plugins.db.jooq.Tables.BOOK_AUTHOR;
import static org.a1cey.bookshelf_pro_plugins.db.jooq.Tables.MEDIA_ITEM;
import static org.a1cey.bookshelf_pro_plugins.db.jooq.Tables.MEDIA_ITEM_LANGUAGE;
import static org.a1cey.bookshelf_pro_plugins.db.jooq.Tables.MOVIE;
import static org.a1cey.bookshelf_pro_plugins.db.jooq.Tables.MOVIE_ACTOR;
import static org.a1cey.bookshelf_pro_plugins.db.jooq.Tables.MOVIE_DIRECTOR;
import static org.a1cey.bookshelf_pro_plugins.db.jooq.Tables.MOVIE_STUDIO;

@Repository
public class JooqMediaItemRepository implements MediaItemRepository {
    private final DSLContext dsl;

    public JooqMediaItemRepository(DSLContext dsl) {
        this.dsl = dsl;
    }

    @Override
    public Optional<MediaItem> findById(Id id) {
        var mediaItemRecord = dsl.fetchOne(MEDIA_ITEM, MEDIA_ITEM.ID.eq(id.value()));

        if (mediaItemRecord == null) {
            return Optional.empty();
        }

        return switch (MediaItemType.valueOf(mediaItemRecord.getType())) {
            case BOOK -> Optional.of(fetchBook(mediaItemRecord));
            case MOVIE -> Optional.of(fetchMovie(mediaItemRecord));
        };
    }

    @Override
    public Set<? extends MediaItem> findByType(MediaItemType type) {
        return switch (type) {
            case MediaItemType.BOOK -> fetchAllBooks();
            case MediaItemType.MOVIE -> fetchAllMovies();
        };
    }

    @Override
    public Set<? extends MediaItem> findByOwner(AccountId owner) {
        return dsl.fetch(MEDIA_ITEM, MEDIA_ITEM.OWNER_ID.eq(owner.value()))
                  .stream()
                  .map(mediaItemRecord -> switch (MediaItemType.valueOf(mediaItemRecord.getType())) {
                      case MediaItemType.BOOK -> fetchBook(mediaItemRecord);
                      case MediaItemType.MOVIE -> fetchMovie(mediaItemRecord);
                  }).collect(Collectors.toSet());
    }

    @Transactional
    @Override
    public void save(MediaItem mediaItem) {
        dsl.insertInto(MEDIA_ITEM)
           .set(MEDIA_ITEM.ID, mediaItem.id().value())
           .set(MEDIA_ITEM.OWNER_ID, mediaItem.owner().value())
           .set(
               MEDIA_ITEM.COVER_IMAGE_URL,
               mediaItem.coverImageUrl() != null ? Objects.requireNonNull(mediaItem.coverImageUrl()).toString() : null
           )
           .set(MEDIA_ITEM.DESCRIPTION, mediaItem.description().description())
           .set(MEDIA_ITEM.TYPE, mediaItem.type().name())
           .set(MEDIA_ITEM.TITLE, mediaItem.title().title())
           .set(MEDIA_ITEM.SUBTITLE, mediaItem.subtitle().subtitle())
           .execute();

        saveLanguages(mediaItem.id(), mediaItem.languages());

        switch (mediaItem) {
            case Book book -> saveBook(book);
            case Movie movie -> saveMovie(movie);
            default -> throw new IllegalStateException("Unexpected media item type: " + mediaItem);
        }

    }

    @Transactional
    @Override
    public void update(MediaItem mediaItem) {
        dsl.update(MEDIA_ITEM)
           .set(
               MEDIA_ITEM.COVER_IMAGE_URL,
               mediaItem.coverImageUrl() != null ? Objects.requireNonNull(mediaItem.coverImageUrl()).toString() : null
           )
           .set(MEDIA_ITEM.DESCRIPTION, mediaItem.description().description())
           .set(MEDIA_ITEM.TITLE, mediaItem.title().title())
           .set(MEDIA_ITEM.SUBTITLE, mediaItem.subtitle().subtitle())
           .where(MEDIA_ITEM.ID.eq(mediaItem.id().value()))
           .execute();

        updateLanguages(mediaItem.id(), mediaItem.languages());

        switch (mediaItem) {
            case Book book -> updateBook(book);
            case Movie movie -> updateMovie(movie);
            default -> throw new IllegalStateException("Unexpected media item type: " + mediaItem);
        }

    }

    @Override
    public Set<? extends MediaItem> search(MediaItemSearchCriteria searchCriteria) {
        // TODO:
        return Set.of();
    }

    private void saveLanguages(MediaItemId id, Set<Language> languages) {
        if (languages.isEmpty()) {
            return;
        }

        dsl.insertInto(MEDIA_ITEM_LANGUAGE, MEDIA_ITEM_LANGUAGE.MEDIA_ITEM_ID, MEDIA_ITEM_LANGUAGE.ISO_CODE)
           .valuesOfRows(languages.stream().map(lang -> DSL.row(id.value(), lang.isoCode())).toList())
           .execute();
    }

    private void updateLanguages(MediaItemId id, Set<Language> languages) {
        if (languages.isEmpty()) {
            return;
        }

        var existingLanguages = dsl
                                    .select(MEDIA_ITEM_LANGUAGE.ISO_CODE)
                                    .from(MEDIA_ITEM_LANGUAGE)
                                    .where(MEDIA_ITEM_LANGUAGE.MEDIA_ITEM_ID.eq(id.value()))
                                    .fetchSet(MEDIA_ITEM_LANGUAGE.ISO_CODE);

        var newLanguages = languages.stream().map(Language::isoCode).collect(Collectors.toSet());

        var languagesToDelete = existingLanguages.stream().filter(lang -> !newLanguages.contains(lang)).toList();
        var languagesToInsert = newLanguages.stream().filter(lang -> !existingLanguages.contains(lang)).toList();

        if (!languagesToDelete.isEmpty()) {
            dsl.deleteFrom(MEDIA_ITEM_LANGUAGE)
               .where(MEDIA_ITEM_LANGUAGE.MEDIA_ITEM_ID.eq(id.value()))
               .and(MEDIA_ITEM_LANGUAGE.ISO_CODE.in(languagesToDelete))
               .execute();
        }

        if (!languagesToInsert.isEmpty()) {
            dsl.insertInto(MEDIA_ITEM_LANGUAGE, MEDIA_ITEM_LANGUAGE.MEDIA_ITEM_ID, MEDIA_ITEM_LANGUAGE.ISO_CODE)
               .valuesOfRows(languagesToInsert.stream().map(isoCode -> DSL.row(id.value(), isoCode)).toList())
               .execute();
        }
    }

    private void saveBook(Book book) {
        dsl.insertInto(BOOK)
           .set(BOOK.ID, book.id().value())
           .set(BOOK.ISBN, book.isbn().value())
           .set(BOOK.PAGE_COUNT, book.pageCount().pageCount())
           .set(BOOK.PUBLISHER, book.publisher().publisher())
           .set(BOOK.PUBLISH_DATE, book.publishDate() != null ? book.publishDate().publishDate() : null)
           .set(BOOK.PUBLISH_PLACE, book.publishPlace().publishPlace())
           .execute();

        saveAuthors(book.id(), book.authors());
    }

    private void updateBook(Book book) {
        dsl.update(BOOK)
           .set(BOOK.PAGE_COUNT, book.pageCount().pageCount())
           .set(BOOK.PUBLISHER, book.publisher().publisher())
           .set(BOOK.PUBLISH_DATE, book.publishDate() != null ? book.publishDate().publishDate() : null)
           .set(BOOK.PUBLISH_PLACE, book.publishPlace().publishPlace())
           .where(BOOK.ID.eq(book.id().value()))
           .execute();

        updateAuthors(book.id(), book.authors());
    }

    private void saveAuthors(MediaItemId id, Set<Author> authors) {
        if (authors.isEmpty()) {
            return;
        }

        dsl.insertInto(BOOK_AUTHOR, BOOK_AUTHOR.BOOK_ID, BOOK_AUTHOR.NAME)
           .valuesOfRows(authors.stream().map(author -> DSL.row(id.value(), author.name())).toList())
           .execute();
    }

    private void updateAuthors(MediaItemId id, Set<Author> authors) {
        if (authors.isEmpty()) {
            return;
        }

        var existingAuthors = dsl
                                  .select(BOOK_AUTHOR.NAME)
                                  .from(BOOK_AUTHOR)
                                  .where(BOOK_AUTHOR.BOOK_ID.eq(id.value()))
                                  .fetchSet(BOOK_AUTHOR.NAME);

        var newAuthors = authors.stream().map(Author::name).collect(Collectors.toSet());

        var authorsToDelete = existingAuthors.stream().filter(author -> !newAuthors.contains(author)).toList();
        var authorsToInsert = newAuthors.stream().filter(author -> !existingAuthors.contains(author)).toList();

        if (!authorsToDelete.isEmpty()) {
            dsl.deleteFrom(BOOK_AUTHOR)
               .where(BOOK_AUTHOR.BOOK_ID.eq(id.value()))
               .and(BOOK_AUTHOR.NAME.in(authorsToDelete))
               .execute();
        }

        if (!authorsToInsert.isEmpty()) {
            dsl.insertInto(BOOK_AUTHOR, BOOK_AUTHOR.BOOK_ID, BOOK_AUTHOR.NAME)
               .valuesOfRows(authorsToInsert.stream().map(name -> DSL.row(id.value(), name)).toList())
               .execute();
        }
    }

    private Book fetchBook(MediaItemRecord mediaItemRecord) {
        var id = mediaItemRecord.getId();
        var bookRecord = dsl.fetchOne(BOOK, BOOK.ID.eq(id));

        if (bookRecord == null) {
            throw new IllegalStateException("Invalid DB state: No book record found for media item " + id);
        }

        var authors = dsl.fetch(BOOK_AUTHOR, BOOK_AUTHOR.BOOK_ID.eq(bookRecord.getId()))
                         .stream()
                         .map(author -> new Author(author.getName()))
                         .collect(Collectors.toSet());

        var languages = dsl.fetch(MEDIA_ITEM_LANGUAGE, MEDIA_ITEM_LANGUAGE.MEDIA_ITEM_ID.eq(id))
                           .stream()
                           .map(lang -> new Language(lang.getIsoCode()))
                           .collect(Collectors.toSet());

        var builder = Book.builder(
                new MediaItemId(id),
                new AccountId(mediaItemRecord.getOwnerId()),
                new Title(mediaItemRecord.getTitle()),
                new Isbn(bookRecord.getIsbn()),
                new PageCount(bookRecord.getPageCount())
            ).authors(authors)
             .languages(languages);

        if (mediaItemRecord.getSubtitle() != null) {
            builder.subtitle(new Subtitle(mediaItemRecord.getSubtitle()));
        }
        if (mediaItemRecord.getDescription() != null) {
            builder.description(new Description(mediaItemRecord.getDescription()));
        }
        if (mediaItemRecord.getCoverImageUrl() != null) {
            builder.coverImageUrl(URI.create(mediaItemRecord.getCoverImageUrl()));
        }
        if (bookRecord.getPublishDate() != null) {
            builder.publishDate(new PublishDate(bookRecord.getPublishDate()));
        }
        if (bookRecord.getPublisher() != null) {
            builder.publisher(new Publisher(bookRecord.getPublisher()));
        }
        if (bookRecord.getPublishPlace() != null) {
            builder.publishPlace(new PublishPlace(bookRecord.getPublishPlace()));
        }

        return builder.build();
    }

    private Set<Book> fetchAllBooks() {
        return dsl.fetch(BOOK).stream().map(bookRecord -> {
            var mediaItemRecord = dsl.fetchOne(MEDIA_ITEM, MEDIA_ITEM.ID.eq(bookRecord.getId()));

            if (mediaItemRecord == null) {
                throw new IllegalStateException("No corresponding media item for book with id " + bookRecord.getId());
            }

            var authors = dsl.fetch(BOOK_AUTHOR, BOOK_AUTHOR.BOOK_ID.eq(bookRecord.getId()))
                             .stream()
                             .map(author -> new Author(author.getName()))
                             .collect(Collectors.toSet());

            var languages = dsl.fetch(MEDIA_ITEM_LANGUAGE, MEDIA_ITEM_LANGUAGE.MEDIA_ITEM_ID.eq(mediaItemRecord.getId()))
                               .stream()
                               .map(lang -> new Language(lang.getIsoCode()))
                               .collect(Collectors.toSet());

            var builder = Book.builder(
                    new MediaItemId(mediaItemRecord.getId()),
                    new AccountId(mediaItemRecord.getOwnerId()),
                    new Title(mediaItemRecord.getTitle()),
                    new Isbn(bookRecord.getIsbn()),
                    new PageCount(bookRecord.getPageCount())
                ).authors(authors)
                 .languages(languages);

            if (mediaItemRecord.getSubtitle() != null) {
                builder.subtitle(new Subtitle(mediaItemRecord.getSubtitle()));
            }
            if (mediaItemRecord.getDescription() != null) {
                builder.description(new Description(mediaItemRecord.getDescription()));
            }
            if (mediaItemRecord.getCoverImageUrl() != null) {
                builder.coverImageUrl(URI.create(mediaItemRecord.getCoverImageUrl()));
            }
            if (bookRecord.getPublishDate() != null) {
                builder.publishDate(new PublishDate(bookRecord.getPublishDate()));
            }
            if (bookRecord.getPublisher() != null) {
                builder.publisher(new Publisher(bookRecord.getPublisher()));
            }
            if (bookRecord.getPublishPlace() != null) {
                builder.publishPlace(new PublishPlace(bookRecord.getPublishPlace()));
            }

            return builder.build();
        }).collect(Collectors.toSet());
    }

    private void saveMovie(Movie movie) {
        dsl.insertInto(MOVIE)
           .set(MOVIE.ID, movie.id().value())
           .set(MOVIE.IMDB_TITLE_ID, movie.imdbTitleId().value())
           .set(MOVIE.DURATION, (int) movie.duration().time().toSeconds())
           .set(MOVIE.ORIGIN_COUNTRY, movie.originCountry().country())
           .set(MOVIE.RELEASE_DATE, movie.releaseDate() != null ? movie.releaseDate().date() : null)
           .execute();

        saveActors(movie.id(), movie.actors());
        saveDirectors(movie.id(), movie.directors());
        saveStudios(movie.id(), movie.studios());
    }

    private void updateMovie(Movie movie) {
        dsl.update(MOVIE)
           .set(MOVIE.ID, movie.id().value())
           .set(MOVIE.IMDB_TITLE_ID, movie.imdbTitleId().value())
           .set(MOVIE.DURATION, (int) movie.duration().time().toSeconds())
           .set(MOVIE.ORIGIN_COUNTRY, movie.originCountry().country())
           .set(MOVIE.RELEASE_DATE, movie.releaseDate() != null ? movie.releaseDate().date() : null)
           .execute();

        updateActors(movie.id(), movie.actors());
        updateDirectors(movie.id(), movie.directors());
        updateStudios(movie.id(), movie.studios());
    }

    private void saveActors(MediaItemId id, Set<Actor> actors) {
        if (actors.isEmpty()) {
            return;
        }

        dsl.insertInto(MOVIE_ACTOR, MOVIE_ACTOR.MOVIE_ID, MOVIE_ACTOR.NAME, MOVIE_ACTOR.ROLE)
           .valuesOfRows(actors.stream().map(actor -> DSL.row(id.value(), actor.name(), actor.role())).toList())
           .execute();
    }

    private void saveDirectors(MediaItemId id, Set<Director> directors) {
        if (directors.isEmpty()) {
            return;
        }

        dsl.insertInto(MOVIE_DIRECTOR, MOVIE_DIRECTOR.MOVIE_ID, MOVIE_DIRECTOR.NAME)
           .valuesOfRows(directors.stream().map(director -> DSL.row(id.value(), director.name())).toList())
           .execute();
    }

    private void saveStudios(MediaItemId id, Set<Studio> studios) {
        if (studios.isEmpty()) {
            return;
        }

        dsl.insertInto(MOVIE_STUDIO, MOVIE_STUDIO.MOVIE_ID, MOVIE_STUDIO.NAME)
           .valuesOfRows(studios.stream().map(studio -> DSL.row(id.value(), studio.name())).toList())
           .execute();
    }

    private void updateActors(MediaItemId id, Set<Actor> actors) {
        if (actors.isEmpty()) {
            return;
        }

        var existingActors = dsl.fetch(MOVIE_ACTOR, MOVIE_ACTOR.MOVIE_ID.eq(id.value()))
                                .stream()
                                .map(record -> new Actor(record.getName(), record.getRole()))
                                .toList();

        var actorsToDelete = existingActors.stream().filter(actor -> !actors.contains(actor)).toList();
        var actorsToInsert = actors.stream().filter(actor -> !existingActors.contains(actor)).toList();

        if (!actorsToDelete.isEmpty()) {
            var rows = actorsToDelete.stream()
                                     .map(actor -> DSL.row(id.value(), actor.name(), actor.role()))
                                     .toList();

            dsl.deleteFrom(MOVIE_ACTOR)
               .where(DSL.row(MOVIE_ACTOR.MOVIE_ID, MOVIE_ACTOR.NAME, MOVIE_ACTOR.ROLE).in(rows))
               .execute();
        }
        if (!actorsToInsert.isEmpty()) {
            dsl.insertInto(MOVIE_ACTOR, MOVIE_ACTOR.MOVIE_ID, MOVIE_ACTOR.NAME, MOVIE_ACTOR.ROLE)
               .valuesOfRows(actorsToInsert.stream().map(actor -> DSL.row(id.value(), actor.name(), actor.role())).toList())
               .execute();
        }

    }

    private void updateDirectors(MediaItemId id, Set<Director> directors) {
        if (directors.isEmpty()) {
            return;
        }

        var existingDirectors = dsl
                                    .select(MOVIE_DIRECTOR.NAME)
                                    .from(MOVIE_DIRECTOR)
                                    .where(MOVIE_DIRECTOR.MOVIE_ID.eq(id.value()))
                                    .fetchSet(MOVIE_DIRECTOR.NAME);

        var newDirectors = directors.stream().map(Director::name).collect(Collectors.toSet());

        var directorsToDelete = existingDirectors.stream().filter(director -> !newDirectors.contains(director)).toList();
        var directorsToInsert = newDirectors.stream().filter(director -> !existingDirectors.contains(director)).toList();

        if (!directorsToDelete.isEmpty()) {
            dsl.deleteFrom(MOVIE_DIRECTOR)
               .where(MOVIE_DIRECTOR.MOVIE_ID.eq(id.value()))
               .and(MOVIE_DIRECTOR.NAME.in(directorsToDelete))
               .execute();
        }

        if (!directorsToInsert.isEmpty()) {
            dsl.insertInto(MOVIE_DIRECTOR, MOVIE_DIRECTOR.MOVIE_ID, MOVIE_DIRECTOR.NAME)
               .valuesOfRows(directorsToInsert.stream().map(name -> DSL.row(id.value(), name)).toList())
               .execute();
        }

    }

    private void updateStudios(MediaItemId id, Set<Studio> studios) {
        if (studios.isEmpty()) {
            return;
        }

        var existingStudios = dsl
                                  .select(MOVIE_STUDIO.NAME)
                                  .from(MOVIE_STUDIO)
                                  .where(MOVIE_STUDIO.MOVIE_ID.eq(id.value()))
                                  .fetchSet(MOVIE_STUDIO.NAME);

        var newStudios = studios.stream().map(Studio::name).collect(Collectors.toSet());

        var studiosToDelete = existingStudios.stream().filter(studio -> !newStudios.contains(studio)).toList();
        var studiosToInsert = newStudios.stream().filter(studio -> !existingStudios.contains(studio)).toList();

        if (!studiosToDelete.isEmpty()) {
            dsl.deleteFrom(MOVIE_STUDIO)
               .where(MOVIE_STUDIO.MOVIE_ID.eq(id.value()))
               .and(MOVIE_STUDIO.NAME.in(studiosToDelete))
               .execute();
        }

        if (!studiosToInsert.isEmpty()) {
            dsl.insertInto(MOVIE_STUDIO, MOVIE_STUDIO.MOVIE_ID, MOVIE_STUDIO.NAME)
               .valuesOfRows(studiosToInsert.stream().map(name -> DSL.row(id.value(), name)).toList())
               .execute();
        }

    }

    private Movie fetchMovie(MediaItemRecord mediaItemRecord) {
        var id = mediaItemRecord.getId();
        var movieRecord = dsl.fetchOne(MOVIE, MOVIE.ID.eq(id));

        if (movieRecord == null) {
            throw new IllegalStateException("Invalid DB state: No movie record found for media item " + id);
        }

        var actors = dsl.fetch(MOVIE_ACTOR, MOVIE_ACTOR.MOVIE_ID.eq(movieRecord.getId()))
                        .stream()
                        .map(actor -> new Actor(actor.getName(), actor.getRole()))
                        .collect(Collectors.toSet());
        var directors = dsl.fetch(MOVIE_DIRECTOR, MOVIE_DIRECTOR.MOVIE_ID.eq(movieRecord.getId()))
                           .stream()
                           .map(director -> new Director(director.getName()))
                           .collect(Collectors.toSet());

        var studios = dsl.fetch(MOVIE_STUDIO, MOVIE_STUDIO.MOVIE_ID.eq(movieRecord.getId()))
                         .stream()
                         .map(studio -> new Studio(studio.getName()))
                         .collect(Collectors.toSet());

        var languages = dsl.fetch(MEDIA_ITEM_LANGUAGE, MEDIA_ITEM_LANGUAGE.MEDIA_ITEM_ID.eq(id))
                           .stream()
                           .map(lang -> new Language(lang.getIsoCode()))
                           .collect(Collectors.toSet());

        var builder = Movie.builder(
                new MediaItemId(id),
                new AccountId(mediaItemRecord.getOwnerId()),
                new Title(mediaItemRecord.getTitle()),
                new ImdbTitleId(movieRecord.getImdbTitleId()),
                Duration.of(movieRecord.getDuration())
            ).actors(actors)
             .directors(directors)
             .studios(studios)
             .languages(languages);

        if (mediaItemRecord.getSubtitle() != null) {
            builder.subtitle(new Subtitle(mediaItemRecord.getSubtitle()));
        }
        if (mediaItemRecord.getDescription() != null) {
            builder.description(new Description(mediaItemRecord.getDescription()));
        }
        if (mediaItemRecord.getCoverImageUrl() != null) {
            builder.coverImageUrl(URI.create(mediaItemRecord.getCoverImageUrl()));
        }
        if (movieRecord.getReleaseDate() != null) {
            builder.releaseDate(new ReleaseDate(movieRecord.getReleaseDate()));
        }
        if (movieRecord.getOriginCountry() != null) {
            builder.originCountry(new OriginCountry(movieRecord.getOriginCountry()));
        }

        return builder.build();
    }

    private Set<Movie> fetchAllMovies() {
        return dsl.fetch(MOVIE).stream().map(movieRecord -> {
            var mediaItemRecord = dsl.fetchOne(MEDIA_ITEM, MEDIA_ITEM.ID.eq(movieRecord.getId()));

            if (mediaItemRecord == null) {
                throw new IllegalStateException("No corresponding media item for movie with id " + movieRecord.getId());
            }

            var actors = dsl.fetch(MOVIE_ACTOR, MOVIE_ACTOR.MOVIE_ID.eq(movieRecord.getId()))
                            .stream()
                            .map(actor -> new Actor(actor.getName(), actor.getRole()))
                            .collect(Collectors.toSet());
            var directors = dsl.fetch(MOVIE_DIRECTOR, MOVIE_DIRECTOR.MOVIE_ID.eq(movieRecord.getId()))
                               .stream()
                               .map(director -> new Director(director.getName()))
                               .collect(Collectors.toSet());

            var studios = dsl.fetch(MOVIE_STUDIO, MOVIE_STUDIO.MOVIE_ID.eq(movieRecord.getId()))
                             .stream()
                             .map(studio -> new Studio(studio.getName()))
                             .collect(Collectors.toSet());

            var languages = dsl.fetch(MEDIA_ITEM_LANGUAGE, MEDIA_ITEM_LANGUAGE.MEDIA_ITEM_ID.eq(movieRecord.getId()))
                               .stream()
                               .map(lang -> new Language(lang.getIsoCode()))
                               .collect(Collectors.toSet());

            var builder = Movie.builder(
                    new MediaItemId(movieRecord.getId()),
                    new AccountId(mediaItemRecord.getOwnerId()),
                    new Title(mediaItemRecord.getTitle()),
                    new ImdbTitleId(movieRecord.getImdbTitleId()),
                    Duration.of(movieRecord.getDuration())
                ).actors(actors)
                 .directors(directors)
                 .studios(studios)
                 .languages(languages);

            if (mediaItemRecord.getSubtitle() != null) {
                builder.subtitle(new Subtitle(mediaItemRecord.getSubtitle()));
            }
            if (mediaItemRecord.getDescription() != null) {
                builder.description(new Description(mediaItemRecord.getDescription()));
            }
            if (mediaItemRecord.getCoverImageUrl() != null) {
                builder.coverImageUrl(URI.create(mediaItemRecord.getCoverImageUrl()));
            }
            if (movieRecord.getReleaseDate() != null) {
                builder.releaseDate(new ReleaseDate(movieRecord.getReleaseDate()));
            }
            if (movieRecord.getOriginCountry() != null) {
                builder.originCountry(new OriginCountry(movieRecord.getOriginCountry()));
            }

            return builder.build();
        }).collect(Collectors.toSet());
    }

}
