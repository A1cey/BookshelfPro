package org.a1cey.bookshelf_pro_plugins.db;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
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
import org.a1cey.bookshelf_pro_domain.media_item.book.BookSearchCriteria;
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
import org.a1cey.bookshelf_pro_domain.media_item.movie.MovieSearchCriteria;
import org.a1cey.bookshelf_pro_domain.media_item.movie.OriginCountry;
import org.a1cey.bookshelf_pro_domain.media_item.movie.ReleaseDate;
import org.a1cey.bookshelf_pro_domain.media_item.movie.Studio;
import org.a1cey.bookshelf_pro_plugins.db.jooq.tables.records.BookRecord;
import org.a1cey.bookshelf_pro_plugins.db.jooq.tables.records.MediaItemRecord;
import org.a1cey.bookshelf_pro_plugins.db.jooq.tables.records.MovieRecord;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.jspecify.annotations.Nullable;
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

    @Transactional(readOnly = true)
    @Override
    public Optional<MediaItem> findById(Id id) {
        return dsl.fetchOptional(MEDIA_ITEM, MEDIA_ITEM.ID.eq(id.value()))
                  .map(record -> switch (MediaItemType.valueOf(record.getType())) {
                      case BOOK -> fetchBook(record);
                      case MOVIE -> fetchMovie(record);
                  });
    }

    @Transactional(readOnly = true)
    @Override
    public Set<? extends MediaItem> findByType(MediaItemType type) {
        return switch (type) {
            case BOOK -> fetchAllBooks();
            case MOVIE -> fetchAllMovies();
        };
    }

    @Transactional(readOnly = true)
    @Override
    public Set<? extends MediaItem> findByOwner(AccountId owner) {
        return dsl.fetch(MEDIA_ITEM, MEDIA_ITEM.OWNER_ID.eq(owner.value()))
                  .stream()
                  .map(record -> switch (MediaItemType.valueOf(record.getType())) {
                      case BOOK -> fetchBook(record);
                      case MOVIE -> fetchMovie(record);
                  })
                  .collect(Collectors.toSet());
    }

    @Transactional
    @Override
    public void save(MediaItem mediaItem) {
        dsl.insertInto(MEDIA_ITEM)
           .set(MEDIA_ITEM.ID, mediaItem.id().value())
           .set(MEDIA_ITEM.OWNER_ID, mediaItem.owner().value())
           .set(MEDIA_ITEM.COVER_IMAGE_URL, uriToString(mediaItem.coverImageUrl()))
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
           .set(MEDIA_ITEM.COVER_IMAGE_URL, uriToString(mediaItem.coverImageUrl()))
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

    @Transactional(readOnly = true)
    @Override
    public Set<? extends MediaItem> search(MediaItemSearchCriteria searchCriteria) {
        return dsl.selectFrom(MEDIA_ITEM)
                  .where(createSearchCondition(searchCriteria))
                  .stream()
                  .map(record -> switch (MediaItemType.valueOf(record.getType())) {
                      case BOOK -> fetchBook(record);
                      case MOVIE -> fetchMovie(record);
                  })
                  .collect(Collectors.toSet());
    }

    private Condition createSearchCondition(MediaItemSearchCriteria searchCriteria) {
        Condition cond = DSL.trueCondition();

        if (searchCriteria.titleFragment().isPresent()) {
            cond = cond.and(MEDIA_ITEM.TITLE.contains(searchCriteria.titleFragment().get()));
        }

        if (searchCriteria.subtitleFragment().isPresent()) {
            cond = cond.and(MEDIA_ITEM.SUBTITLE.contains(searchCriteria.subtitleFragment().get()));
        }

        if (searchCriteria.languages().isPresent()) {
            var ids = dsl.select(MEDIA_ITEM_LANGUAGE.MEDIA_ITEM_ID)
                         .from(MEDIA_ITEM_LANGUAGE)
                         .where(MEDIA_ITEM_LANGUAGE.ISO_CODE.in(searchCriteria.languages().get().stream().map(Language::isoCode).toList()));

            cond = cond.and(MEDIA_ITEM.ID.in(ids));
        }

        if (searchCriteria.mediaItemType().isPresent()) {
            cond = cond.and(MEDIA_ITEM.TYPE.eq(searchCriteria.mediaItemType().get().name()));
        }

        if (searchCriteria.typeCriteria().isPresent()) {
            cond = switch (searchCriteria.typeCriteria().get()) {
                case BookSearchCriteria bookSearchCriteria -> addBookSearchConditions(cond, bookSearchCriteria);
                case MovieSearchCriteria movieSearchCriteria -> addMovieSearchConditions(cond, movieSearchCriteria);
                default -> throw new IllegalStateException("Unexpected search criteria type: " + searchCriteria.typeCriteria().get());
            };
        }

        return cond;
    }

    private Condition addBookSearchConditions(Condition cond, BookSearchCriteria searchCriteria) {
        List<Condition> conds = new ArrayList<>();

        if (searchCriteria.isbn().isPresent()) {
            conds.add(BOOK.ISBN.eq(searchCriteria.isbn().get().value()));
        }
        if (searchCriteria.publishDate().isPresent()) {
            conds.add(BOOK.PUBLISH_DATE.eq(searchCriteria.publishDate().get().publishDate()));
        }
        if (searchCriteria.publisher().isPresent()) {
            conds.add(BOOK.PUBLISHER.eq(searchCriteria.publisher().get().publisher()));
        }
        if (searchCriteria.publishPlace().isPresent()) {
            conds.add(BOOK.PUBLISH_PLACE.eq(searchCriteria.publishPlace().get().publishPlace()));
        }
        if (searchCriteria.pageCount().isPresent()) {
            conds.add(BOOK.PAGE_COUNT.eq(searchCriteria.pageCount().get().pageCount()));
        }

        if (!conds.isEmpty()) {
            cond = cond.and(MEDIA_ITEM.ID.in(dsl.select(BOOK.ID).from(BOOK).where(DSL.and(conds))));
        }

        if (searchCriteria.authors().isPresent()) {
            var ids = dsl.select(BOOK_AUTHOR.BOOK_ID)
                         .from(BOOK_AUTHOR)
                         .where(BOOK_AUTHOR.NAME.in(searchCriteria.authors().get().stream().map(Author::name).toList()));

            cond = cond.and(MEDIA_ITEM.ID.in(ids));
        }

        return cond;
    }

    private Condition addMovieSearchConditions(Condition cond, MovieSearchCriteria searchCriteria) {
        List<Condition> conds = new ArrayList<>();

        if (searchCriteria.imdbTitleId().isPresent()) {
            conds.add(MOVIE.IMDB_TITLE_ID.eq(searchCriteria.imdbTitleId().get().value()));
        }
        if (searchCriteria.releaseDate().isPresent()) {
            conds.add(MOVIE.RELEASE_DATE.eq(searchCriteria.releaseDate().get().date()));
        }
        if (searchCriteria.originCountry().isPresent()) {
            conds.add(MOVIE.ORIGIN_COUNTRY.eq(searchCriteria.originCountry().get().country()));
        }
        if (searchCriteria.duration().isPresent()) {
            conds.add(MOVIE.DURATION.eq((int) searchCriteria.duration().get().time().toSeconds()));
        }

        if (!conds.isEmpty()) {
            cond = cond.and(MEDIA_ITEM.ID.in(dsl.select(MOVIE.ID).from(MOVIE).where(DSL.and(conds))));
        }

        if (searchCriteria.actors().isPresent()) {
            var ids = dsl.select(MOVIE_ACTOR.MOVIE_ID)
                         .from(MOVIE_ACTOR)
                         .where(DSL.row(MOVIE_ACTOR.NAME, MOVIE_ACTOR.ROLE).in(searchCriteria.actors()// Name and role
                                                                                             .get()
                                                                                             .stream()
                                                                                             .map(actor -> DSL.row(
                                                                                                 actor.name(),
                                                                                                 actor.role()
                                                                                             ))
                                                                                             .toList()));

            cond = cond.and(MEDIA_ITEM.ID.in(ids));
        }
        if (searchCriteria.directors().isPresent()) {
            var ids = dsl.select(MOVIE_DIRECTOR.MOVIE_ID)
                         .from(MOVIE_DIRECTOR)
                         .where(MOVIE_DIRECTOR.NAME.in(searchCriteria.directors().get().stream().map(Director::name).toList()));

            cond = cond.and(MEDIA_ITEM.ID.in(ids));
        }
        if (searchCriteria.studios().isPresent()) {
            var ids = dsl.select(MOVIE_STUDIO.MOVIE_ID)
                         .from(MOVIE_STUDIO)
                         .where(MOVIE_STUDIO.NAME.in(searchCriteria.studios().get().stream().map(Studio::name).toList()));

            cond = cond.and(MEDIA_ITEM.ID.in(ids));
        }

        return cond;
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
        var existingLanguages = dsl.select(MEDIA_ITEM_LANGUAGE.ISO_CODE)
                                   .from(MEDIA_ITEM_LANGUAGE)
                                   .where(MEDIA_ITEM_LANGUAGE.MEDIA_ITEM_ID.eq(id.value()))
                                   .fetchSet(MEDIA_ITEM_LANGUAGE.ISO_CODE);

        var newLanguages = languages.stream().map(Language::isoCode).collect(Collectors.toSet());

        updateCollection(
            existingLanguages,
            newLanguages,
            languagesToDelete -> dsl.deleteFrom(MEDIA_ITEM_LANGUAGE)
                                    .where(MEDIA_ITEM_LANGUAGE.MEDIA_ITEM_ID.eq(id.value()))
                                    .and(MEDIA_ITEM_LANGUAGE.ISO_CODE.in(languagesToDelete))
                                    .execute(),
            languagesToInsert -> dsl.insertInto(
                                        MEDIA_ITEM_LANGUAGE,
                                        MEDIA_ITEM_LANGUAGE.MEDIA_ITEM_ID,
                                        MEDIA_ITEM_LANGUAGE.ISO_CODE
                                    )
                                    .valuesOfRows(languagesToInsert.stream().map(code -> DSL.row(id.value(), code)).toList())
                                    .execute()
        );
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
        var existingAuthors = dsl.select(BOOK_AUTHOR.NAME)
                                 .from(BOOK_AUTHOR)
                                 .where(BOOK_AUTHOR.BOOK_ID.eq(id.value()))
                                 .fetchSet(BOOK_AUTHOR.NAME);

        var newAuthors = authors.stream().map(Author::name).collect(Collectors.toSet());

        updateCollection(
            existingAuthors,
            newAuthors,
            authorsToDelete -> dsl.deleteFrom(BOOK_AUTHOR)
                                  .where(BOOK_AUTHOR.BOOK_ID.eq(id.value()))
                                  .and(BOOK_AUTHOR.NAME.in(authorsToDelete))
                                  .execute(),
            authorsToInsert -> dsl.insertInto(BOOK_AUTHOR, BOOK_AUTHOR.BOOK_ID, BOOK_AUTHOR.NAME)
                                  .valuesOfRows(authorsToInsert.stream().map(name -> DSL.row(id.value(), name)).toList())
                                  .execute()
        );
    }

    private Book fetchBook(MediaItemRecord mediaItemRecord) {
        var id = mediaItemRecord.getId();
        var bookRecord = dsl.fetchOne(BOOK, BOOK.ID.eq(id));

        if (bookRecord == null) {
            throw new IllegalStateException("Invalid DB state: No book record found for media item " + id);
        }

        return recreateBook(mediaItemRecord, bookRecord);
    }

    private Set<Book> fetchAllBooks() {
        return dsl.fetch(BOOK).stream().map(bookRecord -> {
            var mediaItemRecord = dsl.fetchOne(MEDIA_ITEM, MEDIA_ITEM.ID.eq(bookRecord.getId()));

            if (mediaItemRecord == null) {
                throw new IllegalStateException("No corresponding media item for book with id " + bookRecord.getId());
            }

            return recreateBook(mediaItemRecord, bookRecord);
        }).collect(Collectors.toSet());
    }

    private Book recreateBook(MediaItemRecord mediaItemRecord, BookRecord bookRecord) {
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
           .set(MOVIE.IMDB_TITLE_ID, movie.imdbTitleId().value())
           .set(MOVIE.DURATION, (int) movie.duration().time().toSeconds())
           .set(MOVIE.ORIGIN_COUNTRY, movie.originCountry().country())
           .set(MOVIE.RELEASE_DATE, movie.releaseDate() != null ? movie.releaseDate().date() : null)
           .where(MOVIE.ID.eq(movie.id().value()))
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
        var existingActors = dsl.fetch(MOVIE_ACTOR, MOVIE_ACTOR.MOVIE_ID.eq(id.value()))
                                .stream()
                                .map(r -> new Actor(r.getName(), r.getRole()))
                                .toList();

        updateCollection(
            existingActors,
            actors,
            actorsToDelete -> dsl.deleteFrom(MOVIE_ACTOR)
                                 .where(DSL.row(MOVIE_ACTOR.MOVIE_ID, MOVIE_ACTOR.NAME, MOVIE_ACTOR.ROLE)
                                           .in(actorsToDelete
                                                   .stream()
                                                   .map(a -> DSL.row(id.value(), a.name(), a.role()))
                                                   .toList()))
                                 .execute(),
            actorsToInsert -> dsl.insertInto(
                                     MOVIE_ACTOR,
                                     MOVIE_ACTOR.MOVIE_ID,
                                     MOVIE_ACTOR.NAME,
                                     MOVIE_ACTOR.ROLE
                                 )
                                 .valuesOfRows(actorsToInsert
                                                   .stream()
                                                   .map(a -> DSL.row(id.value(), a.name(), a.role()))
                                                   .toList())
                                 .execute()
        );
    }

    private void updateDirectors(MediaItemId id, Set<Director> directors) {
        var existingDirectors = dsl.select(MOVIE_DIRECTOR.NAME)
                                   .from(MOVIE_DIRECTOR)
                                   .where(MOVIE_DIRECTOR.MOVIE_ID.eq(id.value()))
                                   .fetchSet(MOVIE_DIRECTOR.NAME);

        var newDirectors = directors.stream().map(Director::name).collect(Collectors.toSet());

        updateCollection(
            existingDirectors,
            newDirectors,
            directorsToDelete -> dsl.deleteFrom(MOVIE_DIRECTOR)
                                    .where(MOVIE_DIRECTOR.MOVIE_ID.eq(id.value()))
                                    .and(MOVIE_DIRECTOR.NAME.in(directorsToDelete))
                                    .execute(),
            directorsToInsert -> dsl.insertInto(MOVIE_DIRECTOR, MOVIE_DIRECTOR.MOVIE_ID, MOVIE_DIRECTOR.NAME)
                                    .valuesOfRows(directorsToInsert.stream().map(name -> DSL.row(id.value(), name)).toList())
                                    .execute()
        );
    }

    private void updateStudios(MediaItemId id, Set<Studio> studios) {
        var existingStudios = dsl.select(MOVIE_STUDIO.NAME)
                                 .from(MOVIE_STUDIO)
                                 .where(MOVIE_STUDIO.MOVIE_ID.eq(id.value()))
                                 .fetchSet(MOVIE_STUDIO.NAME);

        var newStudios = studios.stream().map(Studio::name).collect(Collectors.toSet());

        updateCollection(
            existingStudios,
            newStudios,
            studiosToDelete -> dsl.deleteFrom(MOVIE_STUDIO)
                                  .where(MOVIE_STUDIO.MOVIE_ID.eq(id.value()))
                                  .and(MOVIE_STUDIO.NAME.in(studiosToDelete))
                                  .execute(),
            studiosToInsert -> dsl.insertInto(MOVIE_STUDIO, MOVIE_STUDIO.MOVIE_ID, MOVIE_STUDIO.NAME)
                                  .valuesOfRows(studiosToInsert.stream().map(name -> DSL.row(id.value(), name)).toList())
                                  .execute()
        );
    }

    private Movie fetchMovie(MediaItemRecord mediaItemRecord) {
        var id = mediaItemRecord.getId();
        var movieRecord = dsl.fetchOne(MOVIE, MOVIE.ID.eq(id));

        if (movieRecord == null) {
            throw new IllegalStateException("Invalid DB state: No movie record found for media item " + id);
        }

        return recreateMovie(mediaItemRecord, movieRecord);
    }

    private Set<Movie> fetchAllMovies() {
        return dsl.fetch(MOVIE).stream().map(movieRecord -> {
            var mediaItemRecord = dsl.fetchOne(MEDIA_ITEM, MEDIA_ITEM.ID.eq(movieRecord.getId()));

            if (mediaItemRecord == null) {
                throw new IllegalStateException("No corresponding media item for movie with id " + movieRecord.getId());
            }

            return recreateMovie(mediaItemRecord, movieRecord);
        }).collect(Collectors.toSet());
    }

    private Movie recreateMovie(MediaItemRecord mediaItemRecord, MovieRecord movieRecord) {
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

        var languages = dsl.fetch(MEDIA_ITEM_LANGUAGE, MEDIA_ITEM_LANGUAGE.MEDIA_ITEM_ID.eq(mediaItemRecord.getId()))
                           .stream()
                           .map(lang -> new Language(lang.getIsoCode()))
                           .collect(Collectors.toSet());

        var builder = Movie.builder(
                new MediaItemId(mediaItemRecord.getId()),
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

    private @Nullable String uriToString(@Nullable URI uri) {
        return uri != null ? uri.toString() : null;
    }

    private <T> void updateCollection(
        Collection<T> existingList,
        Collection<T> newList,
        Consumer<Collection<T>> deleteFn,
        Consumer<Collection<T>> insertFn
    ) {
        var toDelete = existingList.stream().filter(e -> !newList.contains(e)).toList();
        var toInsert = newList.stream().filter(e -> !existingList.contains(e)).toList();

        if (!toDelete.isEmpty()) {
            deleteFn.accept(toDelete);
        }
        if (!toInsert.isEmpty()) {
            insertFn.accept(toInsert);
        }
    }

}
