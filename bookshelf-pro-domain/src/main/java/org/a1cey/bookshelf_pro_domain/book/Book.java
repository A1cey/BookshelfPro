package org.a1cey.bookshelf_pro_domain.book;

import jakarta.validation.Valid;
import org.a1cey.bookshelf_pro_domain.ID;
import org.a1cey.bookshelf_pro_domain.Label;
import org.a1cey.bookshelf_pro_domain.PublishDate;
import org.a1cey.bookshelf_pro_domain.Title;
import org.a1cey.bookshelf_pro_domain.media_item.MediaItem;
import org.jmolecules.ddd.annotation.AggregateRoot;
import org.jspecify.annotations.Nullable;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@AggregateRoot
public final class Book extends MediaItem {

    @Valid
    private final ISBN isbn;
    private final String subtitle;
    private final List<Author> authors;
    @Valid
    @Nullable
    private final PublishDate publishDate;
    private final String publisher;
    private final String publishPlace;
    private final PageCount pageCount;

    private Book(
            ID id,
            Title title,
            @Nullable URI coverImageUrl,
            String description,
            List<Label> labels,
            ISBN isbn,
            String subtitle,
            List<Author> authors,
            @Nullable PublishDate publishDate,
            String publisher,
            String publishPlace,
            PageCount pageCount) {


        super(id, title, coverImageUrl, description, labels);
        this.isbn = isbn;
        this.subtitle = subtitle;
        this.authors = List.copyOf(authors); // prevent modification from outside
        this.publishDate = publishDate;
        this.publisher = publisher;
        this.publishPlace = publishPlace;
        this.pageCount = pageCount;
    }

    public ISBN getIsbn() {
        return isbn;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public List<Author> getAuthors() {
        return authors;
    }

    public @Nullable PublishDate getPublishDate() {
        return publishDate;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getPublishPlace() {
        return publishPlace;
    }

    public PageCount getPageCount() {
        return pageCount;
    }

    public static class BookBuilder {

        private final ID id;
        private final Title title;
        private final ISBN isbn;
        private List<Author> authors = new ArrayList<>();
        private final PageCount pageCount;
        private @Nullable URI coverImageUrl;
        private String description = "";
        private List<Label> labels = new ArrayList<>();
        private String subtitle = "";
        private @Nullable PublishDate publishDate = null;
        private String publisher = "";
        private String publishPlace = "";

        public BookBuilder(ID id, Title title, ISBN isbn, PageCount pageCount) {
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

        public BookBuilder description(String description) {
            this.description = description;
            return this;
        }

        public BookBuilder labels(List<Label> labels) {
            this.labels = new ArrayList<>(labels); // Create a mutable defensive copy
            return this;
        }

        public BookBuilder label(Label label) {
            this.labels.add(label);
            return this;
        }

        public BookBuilder subtitle(String subtitle) {
            this.subtitle = subtitle;
            return this;
        }

        public BookBuilder publishDate(@Nullable PublishDate publishDate) {
            this.publishDate = publishDate;
            return this;
        }

        public BookBuilder publisher(String publisher) {
            this.publisher = publisher;
            return this;
        }

        public BookBuilder publishPlace(String publishPlace) {
            this.publishPlace = publishPlace;
            return this;
        }

        public Book build() {
            return new Book(
                    id,
                    title,
                    coverImageUrl,
                    description,
                    labels,
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
