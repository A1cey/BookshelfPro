package org.a1cey.bookshelf_pro_plugins.rest.bookshelf.request;

import java.util.Set;
import java.util.UUID;

public record AddBookshelfEntryRequest(
    UUID accountId,
    String name,
    String password,
    UUID mediaItemId,
    Set<String> labels
) {}
