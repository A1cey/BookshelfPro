package org.a1cey.bookshelfpro.domain;

import jakarta.validation.Valid;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import org.jmolecules.ddd.annotation.AggregateRoot;
import org.jspecify.annotations.Nullable;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@AggregateRoot
public final class Book extends MediaItem {

    @Valid
    private final ISBN isbn;
    private final String subtitle;
    private final List<String> authors;
    @PastOrPresent
    private final LocalDate publishDate;
    private final String publisher;
    private final String publishPlace;
    @Positive
    private final int pageCount;

    public Book(
            ID id,
            String title,
            @Nullable URI coverImageUrl,
            String description,
            List<Label> labels,
            ISBN isbn,
            String subtitle,
            List<String> authors,
            @PastOrPresent LocalDate publishDate,
            String publisher,
            String publishPlace,
            @Positive int pageCount) {
        if (publishDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Publish date cannot be in the future");
        }
        if (pageCount <= 0) {
            throw new IllegalArgumentException("Page count must be a positive integer");
        }

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

    public List<String> getAuthors() {
        return authors;
    }

    public LocalDate getPublishDate() {
        return publishDate;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getPublishPlace() {
        return publishPlace;
    }

    public int getPageCount() {
        return pageCount;
    }

}
