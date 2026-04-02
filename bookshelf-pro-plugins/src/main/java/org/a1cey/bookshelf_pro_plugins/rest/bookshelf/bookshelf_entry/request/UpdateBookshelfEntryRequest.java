package org.a1cey.bookshelf_pro_plugins.rest.bookshelf.bookshelf_entry.request;

import java.util.Set;

import org.jspecify.annotations.Nullable;

public record UpdateBookshelfEntryRequest(
    @Nullable Integer consumptionProgressNumber,
    @Nullable Set<String> labels
) {}
