package org.a1cey.bookshelf_pro_plugins.rest.media_item.book.request;

import java.net.URI;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

import org.jspecify.annotations.Nullable;

public record CreateBookRequest(
    UUID accountId, // TODO: Replace this with real auth
    String name,
    String password,
    String title,
    String isbn,
    int pageCount,
    @Nullable String subtitle,
    @Nullable String description,
    @Nullable URI coverImageUrl,
    @Nullable Set<String> authors,
    @Nullable LocalDate publishDate,
    @Nullable String publisher,
    @Nullable String publishPlace,
    @Nullable Set<String> languages
) {}
