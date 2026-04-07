package org.a1cey.bookshelf_pro_application.dto;

import java.net.URI;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.a1cey.bookshelf_pro_domain.media_item.Language;
import org.a1cey.bookshelf_pro_domain.media_item.book.Author;
import org.a1cey.bookshelf_pro_domain.media_item.book.Book;
import org.jspecify.annotations.Nullable;

public record BookDto(
    UUID mediaItemId,
    UUID ownerId,
    String title,
    String subtitle,
    @Nullable URI coverImageUrl,
    String description,
    Set<String> languages,
    String isbn,
    Set<String> authors,
    @Nullable LocalDate publishDate,
    String publisher,
    String publishPlace,
    int pageCount
) implements MediaItemDto {

    public static BookDto from(Book book) {
        return new BookDto(
            book.id().value(),
            book.owner().value(),
            book.title().title(),
            book.subtitle().subtitle(),
            book.coverImageUrl(),
            book.description().description(),
            book.languages().stream().map(Language::isoCode).collect(Collectors.toSet()),
            book.isbn().value(),
            book.authors().stream().map(Author::name).collect(Collectors.toSet()),
            book.publishDate() != null ? book.publishDate().publishDate() : null,
            book.publisher().publisher(),
            book.publishPlace().publishPlace(),
            book.pageCount().pageCount()
        );
    }

}
