package org.a1cey.bookshelf_pro_plugins.rest.media_item.book.request;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import org.jspecify.annotations.Nullable;

public record CreateBookRequest(
    String title,
    String isbn,
    int pageCount,
    @Nullable String subtitle,
    @Nullable String description,
    @Nullable URI coverImageUrl,
    @Nullable List<String> authors,
    @Nullable LocalDate publishDate,
    @Nullable String publisher,
    @Nullable String publishPlace,
    @Nullable Set<String> languages
) {
}
