package org.a1cey.bookshelf_pro_plugins.rest.bookshelf.bookshelf_entry.request;

import java.util.UUID;

public record GetBookshelfEntryRequest(
    UUID accountId,
    String name,
    String password
) {}
