package org.a1cey.bookshelf_pro_domain.media_item.book;

import java.net.URI;
import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import org.a1cey.bookshelf_pro_domain.OwnershipPolicy;
import org.a1cey.bookshelf_pro_domain.Title;
import org.a1cey.bookshelf_pro_domain.account.AccountId;
import org.a1cey.bookshelf_pro_domain.media_item.Description;
import org.a1cey.bookshelf_pro_domain.media_item.Language;
import org.a1cey.bookshelf_pro_domain.media_item.MediaItem;
import org.a1cey.bookshelf_pro_domain.media_item.MediaItemId;
import org.a1cey.bookshelf_pro_domain.media_item.MediaItemType;
import org.a1cey.bookshelf_pro_domain.media_item.Subtitle;
import org.jmolecules.ddd.annotation.AggregateRoot;
import org.jspecify.annotations.Nullable;

import jakarta.validation.Valid;

@AggregateRoot
public final class Book extends MediaItem {

    @Valid
    private final Isbn isbn;
    private final Set<@Valid Author> authors;
    @Valid
    @Nullable
    private PublishDate publishDate;
    private Publisher publisher;
    private PublishPlace publishPlace;
    @Valid
    private PageCount pageCount;

    private Book(
        MediaItemId id,
        @Valid Title title,
        Subtitle subtitle,
        @Nullable URI coverImageUrl,
        Description description,
        AccountId owner,
        Isbn isbn,
        Set<Author> authors,
        @Valid @Nullable PublishDate publishDate,
        Publisher publisher,
        PublishPlace publishPlace,
        @Valid PageCount pageCount,
        Set<Language> languages) {

        super(id, MediaItemType.BOOK, title, subtitle, coverImageUrl, description, owner, languages);
        this.isbn = isbn;
        this.authors = new HashSet<>(authors); // prevent modification from outside
        this.publishDate = publishDate;
        this.publisher = publisher;
        this.publishPlace = publishPlace;
        this.pageCount = pageCount;
    }

    public static BookBuilder builder(MediaItemId id, AccountId owner, @Valid Title title, @Valid Isbn isbn, @Valid PageCount pageCount) {
        return new BookBuilder(id, owner, title, isbn, pageCount);
    }

    public Isbn isbn() {
        return isbn;
    }

    public Set<Author> authors() {
        return Collections.unmodifiableSet(authors);
    }

    public void changeAuthors(Set<@Valid Author> authors, AccountId userRequestingChange) {
        OwnershipPolicy.validate(owner, userRequestingChange, id);
        this.authors.clear();
        this.authors.addAll(authors);
    }

    public void addAuthor(@Valid Author newAuthor, AccountId userRequestingChange) {
        OwnershipPolicy.validate(owner, userRequestingChange, id);
        authors.add(newAuthor);
    }

    public void removeAuthor(@Valid Author authorToRemove, AccountId userRequestingChange) {
        OwnershipPolicy.validate(owner, userRequestingChange, id);
        authors.remove(authorToRemove);
    }

    public @Nullable PublishDate publishDate() {
        return publishDate;
    }

    public void changePublishDate(@Valid @Nullable PublishDate newPublishDate, AccountId userRequestingChange) {
        OwnershipPolicy.validate(owner, userRequestingChange, id);
        publishDate = newPublishDate;
    }

    public Publisher publisher() {
        return publisher;
    }

    public void changePublisher(Publisher newPublisher, AccountId userRequestingChange) {
        OwnershipPolicy.validate(owner, userRequestingChange, id);
        publisher = newPublisher;
    }

    public PublishPlace publishPlace() {
        return publishPlace;
    }

    public void changePublishPlace(PublishPlace newPublishPlace, AccountId userRequestingChange) {
        OwnershipPolicy.validate(owner, userRequestingChange, id);
        publishPlace = newPublishPlace;
    }

    public PageCount pageCount() {
        return pageCount;
    }

    public void changePageCount(@Valid PageCount newPageCount, AccountId userRequestingChange) {
        OwnershipPolicy.validate(owner, userRequestingChange, id);
        pageCount = newPageCount;
    }

    public BookConsumptionProgress createProgress(PageCount currentPage) {
        return new BookConsumptionProgress(currentPage, pageCount);
    }

    public static final class BookBuilder {

        private final MediaItemId id;
        private final AccountId owner;
        private final Title title;
        private final Isbn isbn;
        private final PageCount pageCount;
        private final Set<Language> languages = new HashSet<>();
        private Set<Author> authors = new HashSet<>();
        private @Nullable URI coverImageUrl;
        private Description description = new Description("");
        private Subtitle subtitle = new Subtitle("");
        private @Nullable PublishDate publishDate = null;
        private Publisher publisher = new Publisher("");
        private PublishPlace publishPlace = new PublishPlace("");

        private BookBuilder(MediaItemId id, AccountId owner, @Valid Title title, @Valid Isbn isbn, @Valid PageCount pageCount) {
            this.id = id;
            this.owner = owner;
            this.isbn = isbn;
            this.title = title;
            this.pageCount = pageCount;
            languages.add(Language.of(Locale.ENGLISH));
        }

        public BookBuilder authors(Set<@Valid Author> authors) {
            this.authors = new HashSet<>(authors); // Create a mutable defensive copy
            return this;
        }

        public BookBuilder author(@Valid Author author) {
            this.authors.add(author);
            return this;
        }

        public BookBuilder coverImageUrl(@Nullable URI coverImageUrl) {
            this.coverImageUrl = coverImageUrl;
            return this;
        }

        public BookBuilder description(Description description) {
            this.description = description;
            return this;
        }

        public BookBuilder subtitle(Subtitle subtitle) {
            this.subtitle = subtitle;
            return this;
        }

        public BookBuilder publishDate(@Valid @Nullable PublishDate publishDate) {
            this.publishDate = publishDate;
            return this;
        }

        public BookBuilder publisher(Publisher publisher) {
            this.publisher = publisher;
            return this;
        }

        public BookBuilder publishPlace(PublishPlace publishPlace) {
            this.publishPlace = publishPlace;
            return this;
        }

        public BookBuilder languages(Set<@Valid Language> languages) {
            this.languages.addAll(languages);
            return this;
        }

        public BookBuilder language(@Valid Language language) {
            this.languages.add(language);
            return this;
        }

        public Book build() {
            return new Book(
                id,
                title,
                subtitle,
                coverImageUrl,
                description,
                owner,
                isbn,
                authors,
                publishDate,
                publisher,
                publishPlace,
                pageCount,
                languages
            );
        }

    }

}
