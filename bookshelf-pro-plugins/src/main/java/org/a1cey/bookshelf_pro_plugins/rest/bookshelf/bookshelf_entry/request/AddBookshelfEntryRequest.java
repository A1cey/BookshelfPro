package org.a1cey.bookshelf_pro_plugins.rest.bookshelf.bookshelf_entry.request;

import java.util.Set;
import java.util.UUID;

public record AddBookshelfEntryRequest(
    UUID mediaItemId,
    Set<String> labels
) {}
