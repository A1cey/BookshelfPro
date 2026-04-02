package org.a1cey.bookshelf_pro_plugins.rest.media_item.book.request;

import java.net.URI;
import java.time.LocalDate;
import java.util.Set;

import org.jspecify.annotations.Nullable;

public record UpdateBookRequest(
    @Nullable String title,
    @Nullable String subtitle,
    @Nullable String description,
    @Nullable URI coverImageUrl,
    @Nullable Set<String> languages,
    @Nullable Integer pageCount,
    @Nullable Set<String> authors,
    @Nullable LocalDate publishDate,
    @Nullable String publisher,
    @Nullable String publishPlace
) {}
