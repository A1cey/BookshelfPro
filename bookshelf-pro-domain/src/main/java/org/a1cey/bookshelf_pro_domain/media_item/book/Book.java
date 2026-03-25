package org.a1cey.bookshelf_pro_domain.media_item.book;

import jakarta.validation.Valid;
import org.a1cey.bookshelf_pro_domain.Title;
import org.a1cey.bookshelf_pro_domain.media_item.*;
import org.a1cey.bookshelf_pro_domain.review.ReviewID;
import org.jmolecules.ddd.annotation.AggregateRoot;
import org.jspecify.annotations.Nullable;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@AggregateRoot
public final class Book extends MediaItem {

    @Valid
    private final ISBN isbn;
    private final Subtitle subtitle;
    private final List<Author> authors;
    @Valid
    @Nullable
    private final PublishDate publishDate;
    private final Publisher publisher;
    private final PublishPlace publishPlace;
    private final PageCount pageCount;

    private Book(
            MediaItemID id,
            Title title,
            @Nullable URI coverImageUrl,
            Description description,
            List<ReviewID> reviews,
            ISBN isbn,
            Subtitle subtitle,
            List<Author> authors,
            @Nullable PublishDate publishDate,
            Publisher publisher,
            PublishPlace publishPlace,
            PageCount pageCount) {


        super(id, title, coverImageUrl, description, reviews);
        this.isbn = isbn;
        this.subtitle = subtitle;
        this.authors = List.copyOf(authors); // prevent modification from outside
        this.publishDate = publishDate;
        this.publisher = publisher;
        this.publishPlace = publishPlace;
        this.pageCount = pageCount;
    }

    @Override
    public MediaItemType mediaItemType() {
        return MediaItemType.BOOK;
    }

    public ISBN isbn() {
        return isbn;
    }

    public Subtitle subtitle() {
        return subtitle;
    }

    public List<Author> authors() {
        return authors;
    }

    public @Nullable PublishDate publishDate() {
        return publishDate;
    }

    public Publisher publisher() {
        return publisher;
    }

    public PublishPlace publishPlace() {
        return publishPlace;
    }

    public PageCount pageCount() {
        return pageCount;
    }

    public static BookBuilder builder(MediaItemID id, Title title, ISBN isbn, PageCount pageCount) {
        return new BookBuilder(id, title, isbn, pageCount);
    }

    public static final class BookBuilder {

        private final MediaItemID id;
        private final Title title;
        private final ISBN isbn;
        private List<Author> authors = new ArrayList<>();
        private final PageCount pageCount;
        private @Nullable URI coverImageUrl;
        private Description description = new Description("");
        private List<ReviewID> reviews = new ArrayList<>();
        private Subtitle subtitle = new Subtitle("");
        private @Nullable PublishDate publishDate = null;
        private Publisher publisher = new Publisher("");
        private PublishPlace publishPlace = new PublishPlace("");

        public BookBuilder(MediaItemID id, Title title, ISBN isbn, PageCount pageCount) {
            this.id = id;
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

        public BookBuilder reviews(List<ReviewID> reviews) {
            this.reviews = new ArrayList<>(reviews); // Create a mutable defensive copy
            return this;
        }

        public BookBuilder review(ReviewID reviewID) {
            this.reviews.add(reviewID);
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
                    reviews,
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
