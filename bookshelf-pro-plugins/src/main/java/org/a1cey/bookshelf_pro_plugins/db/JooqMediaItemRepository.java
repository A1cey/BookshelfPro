package org.a1cey.bookshelf_pro_plugins.db;

import java.net.URI;
import java.util.List;
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
import org.a1cey.bookshelf_pro_plugins.db.jooq.tables.records.MediaItemRecord;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import static org.a1cey.bookshelf_pro_plugins.db.jooq.Tables.BOOK;
import static org.a1cey.bookshelf_pro_plugins.db.jooq.Tables.BOOK_AUTHOR;
import static org.a1cey.bookshelf_pro_plugins.db.jooq.Tables.MEDIA_ITEM;
import static org.a1cey.bookshelf_pro_plugins.db.jooq.Tables.MEDIA_ITEM_LANGUAGE;

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
        };
    }

    @Transactional // TODO: Move this to invocating functions?
    @Override
    public void save(MediaItem mediaItem) {
        dsl.insertInto(MEDIA_ITEM)
           .set(MEDIA_ITEM.ID, mediaItem.id().value())
           .set(MEDIA_ITEM.OWNER_ID, mediaItem.owner().value())
           .set(MEDIA_ITEM.COVER_IMAGE_URL, mediaItem.coverImageUrl() != null ? mediaItem.coverImageUrl().toString() : null)
           .set(MEDIA_ITEM.DESCRIPTION, mediaItem.description().description())
           .set(MEDIA_ITEM.TYPE, mediaItem.type().name())
           .set(MEDIA_ITEM.TITLE, mediaItem.title().title())
           .set(MEDIA_ITEM.SUBTITLE, mediaItem.subtitle().subtitle())
           .execute();

        saveLanguages(mediaItem.id(), mediaItem.languages());

        switch (mediaItem) {
            case Book book -> saveBook(book);
            default -> throw new IllegalStateException("Unexpected media item type: " + mediaItem);
        }

    }

    @Override
    public List<MediaItem> search(MediaItemSearchCriteria searchCriteria) {
        // TODO:
        return List.of();
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

    private void saveAuthors(MediaItemId id, Set<Author> authors) {
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

    private void saveLanguages(MediaItemId id, Set<Language> languages) {
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

    private Book fetchBook(MediaItemRecord mediaItemRecord) {
        var id = mediaItemRecord.getId();
        var bookRecord = dsl.fetchOne(BOOK, BOOK.ID.eq(id));

        if (bookRecord == null) {
            throw new RuntimeException("Invalid DB state: No book record found for media item " + id);
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
}
