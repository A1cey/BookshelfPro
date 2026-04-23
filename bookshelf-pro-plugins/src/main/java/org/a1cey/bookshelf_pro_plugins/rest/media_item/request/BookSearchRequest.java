package org.a1cey.bookshelf_pro_plugins.rest.media_item.request;

import java.time.LocalDate;
import java.util.List;

import org.jspecify.annotations.Nullable;

public record BookSearchRequest(
    @Nullable String isbn,
    @Nullable List<String> authors,
    @Nullable LocalDate publishDate,
    @Nullable String publisher,
    @Nullable String publishPlace,
    @Nullable Integer pageCount
) implements SpecificSearchRequest {}
