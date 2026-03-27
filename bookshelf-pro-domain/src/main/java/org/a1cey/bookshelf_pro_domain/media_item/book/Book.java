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

@AggregateRoot
public final class Book extends MediaItem {

    @Valid
    private final ISBN isbn;
    private Subtitle subtitle;
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
            Title title,
            @Nullable URI coverImageUrl,
            Description description,
            AccountID owner,
            ISBN isbn,
            Subtitle subtitle,
            List<Author> authors,
            @Nullable PublishDate publishDate,
            Publisher publisher,
            PublishPlace publishPlace,
            PageCount pageCount) {

        super(id, MediaItemType.BOOK, title, coverImageUrl, description, owner);
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

    public Subtitle subtitle() {
        return subtitle;
    }

    public void changeSubtitle(Subtitle newSubtitle, AccountID userRequestingChange) {
        OwnershipPolicy.validate(owner, userRequestingChange, id);
        subtitle = newSubtitle;
    }

    public List<Author> authors() {
        return Collections.unmodifiableList(authors);
    }

    public void addAuthor(Author newAuthor, AccountID userRequestingChange) {
        OwnershipPolicy.validate(owner, userRequestingChange, id);
        authors.add(newAuthor);
    }

    public void removeAuthor(Author authorToRemove, AccountID userRequestingChange) {
        OwnershipPolicy.validate(owner, userRequestingChange, id);
        authors.remove(authorToRemove);
    }

    public @Nullable PublishDate publishDate() {
        return publishDate;
    }

    public void changePublishDate(@Nullable PublishDate newPublishDate, AccountID userRequestingChange) {
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

    public void changePageCount(PageCount newPageCount, AccountID userRequestingChange) {
        OwnershipPolicy.validate(owner, userRequestingChange, id);
        pageCount = newPageCount;
    }

    public static BookBuilder builder(MediaItemID id, AccountID owner, Title title, ISBN isbn, PageCount pageCount) {
        return new BookBuilder(id, owner, title, isbn, pageCount);
    }

    public static final class BookBuilder {

        private final MediaItemID id;
        private final AccountID owner;
        @Valid
        private final Title title;
        @Valid
        private final ISBN isbn;
        private List<@Valid Author> authors = new ArrayList<>();
        @Valid
        private final PageCount pageCount;
        private @Nullable URI coverImageUrl;
        private Description description = new Description("");
        private Subtitle subtitle = new Subtitle("");
        private @Nullable PublishDate publishDate = null;
        private Publisher publisher = new Publisher("");
        private PublishPlace publishPlace = new PublishPlace("");

        private BookBuilder(MediaItemID id, AccountID owner, Title title, ISBN isbn, PageCount pageCount) {
            this.id = id;
            this.owner = owner;
            this.isbn = isbn;
            this.title = title;
            this.pageCount = pageCount;
        }

        public BookBuilder authors(List<Author> authors) {
            this.authors = new ArrayList<>(authors); // Create a mutable defensive copy
            return this;
        }

        public BookBuilder author(Author author) {
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

        public BookBuilder publishDate(@Nullable PublishDate publishDate) {
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

        public Book build() {
            return new Book(
                    id,
                    title,
                    coverImageUrl,
                    description,
                    owner,
                    isbn,
                    subtitle,
                    authors,
                    publishDate,
                    publisher,
                    publishPlace,
                    pageCount
            );
        }

    }

}
