package org.a1cey.bookshelf_pro_plugins.rest.media_item.book.request;

import java.net.URI;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

import org.jspecify.annotations.Nullable;

public record UpdateBookRequest(
    UUID requestingAccountId, // TODO: Replace this with real auth
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
