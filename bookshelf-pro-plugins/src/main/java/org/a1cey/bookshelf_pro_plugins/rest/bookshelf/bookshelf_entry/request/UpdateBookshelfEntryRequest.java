package org.a1cey.bookshelf_pro_plugins.rest.bookshelf.bookshelf_entry.request;

import java.util.Set;
import java.util.UUID;

import org.jspecify.annotations.Nullable;

public record UpdateBookshelfEntryRequest(
    UUID accountId,
    String name,
    String password,
    @Nullable Integer consumptionProgressNumber,
    @Nullable Set<String> labels
) {}
