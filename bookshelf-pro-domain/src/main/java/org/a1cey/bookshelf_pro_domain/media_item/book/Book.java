package org.a1cey.bookshelf_pro_domain.media_item.book;

import jakarta.validation.Valid;
import org.a1cey.bookshelf_pro_domain.OwnershipPolicy;
import org.a1cey.bookshelf_pro_domain.Title;
import org.a1cey.bookshelf_pro_domain.account.AccountID;
import org.a1cey.bookshelf_pro_domain.media_item.*;
import org.jmolecules.ddd.annotation.AggregateRoot;
import org.jspecify.annotations.Nullable;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

@AggregateRoot
public final class Book extends MediaItem {

    @Valid
    private final ISBN isbn;
    private final List<@Valid Author> authors;
    @Valid
    @Nullable
    private PublishDate publishDate;
    private Publisher publisher;
    private PublishPlace publishPlace;
    @Valid
    private PageCount pageCount;

    private Book(
            MediaItemID id,
            @Valid Title title,
            Subtitle subtitle,
            @Nullable URI coverImageUrl,
            Description description,
            AccountID owner,
            ISBN isbn,
            List<Author> authors,
            @Valid @Nullable PublishDate publishDate,
            Publisher publisher,
            PublishPlace publishPlace,
            @Valid PageCount pageCount,
            Language language) {

        super(id, MediaItemType.BOOK, title,subtitle, coverImageUrl, description, owner, language);
        this.isbn = isbn;
        this.subtitle = subtitle;
        this.authors = new ArrayList<>(authors); // prevent modification from outside
        this.publishDate = publishDate;
        this.publisher = publisher;
        this.publishPlace = publishPlace;
        this.pageCount = pageCount;
    }

    public ISBN isbn() {
        return isbn;
    }


    public List<Author> authors() {
        return Collections.unmodifiableList(authors);
    }

    public void addAuthor(@Valid Author newAuthor, AccountID userRequestingChange) {
        OwnershipPolicy.validate(owner, userRequestingChange, id);
        authors.add(newAuthor);
    }

    public void removeAuthor(@Valid Author authorToRemove, AccountID userRequestingChange) {
        OwnershipPolicy.validate(owner, userRequestingChange, id);
        authors.remove(authorToRemove);
    }

    public @Nullable PublishDate publishDate() {
        return publishDate;
    }

    public void changePublishDate(@Valid @Nullable PublishDate newPublishDate, AccountID userRequestingChange) {
        OwnershipPolicy.validate(owner, userRequestingChange, id);
        publishDate = newPublishDate;
    }

    public Publisher publisher() {
        return publisher;
    }

    public void changePublisher(Publisher newPublisher, AccountID userRequestingChange) {
        OwnershipPolicy.validate(owner, userRequestingChange, id);
        publisher = newPublisher;
    }

    public PublishPlace publishPlace() {
        return publishPlace;
    }

    public void changePublishPlace(PublishPlace newPublishPlace, AccountID userRequestingChange) {
        OwnershipPolicy.validate(owner, userRequestingChange, id);
        publishPlace = newPublishPlace;
    }

    public PageCount pageCount() {
        return pageCount;
    }

    public void changePageCount(@Valid PageCount newPageCount, AccountID userRequestingChange) {
        OwnershipPolicy.validate(owner, userRequestingChange, id);
        pageCount = newPageCount;
    }

    public PageProgress createProgress(PageCount currentPage) {
        return new PageProgress(currentPage, pageCount);
    }


    public static BookBuilder builder(MediaItemID id, AccountID owner,@Valid  Title title,@Valid  ISBN isbn,@Valid  PageCount pageCount) {
        return new BookBuilder(id, owner, title, isbn, pageCount);
    }

    public static final class BookBuilder {

        private final MediaItemID id;
        private final AccountID owner;
        private final Title title;
        private final ISBN isbn;
        private List<Author> authors = new ArrayList<>();
        private final PageCount pageCount;
        private @Nullable URI coverImageUrl;
        private Description description = new Description("");
        private Subtitle subtitle = new Subtitle("");
        private @Nullable PublishDate publishDate = null;
        private Publisher publisher = new Publisher("");
        private PublishPlace publishPlace = new PublishPlace("");
        private Language language = Language.of(Locale.ENGLISH);

        private BookBuilder(MediaItemID id, AccountID owner,@Valid Title title,@Valid ISBN isbn, @Valid PageCount pageCount) {
            this.id = id;
            this.owner = owner;
            this.isbn = isbn;
            this.title = title;
            this.pageCount = pageCount;
        }

        public BookBuilder authors(List<@Valid Author> authors) {
            this.authors = new ArrayList<>(authors); // Create a mutable defensive copy
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

        public BookBuilder language(@Valid Language language) {
            this.language = language;
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
                    language
            );
        }

    }

}
