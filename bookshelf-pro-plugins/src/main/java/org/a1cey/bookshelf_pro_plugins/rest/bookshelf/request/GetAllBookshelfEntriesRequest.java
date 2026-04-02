package org.a1cey.bookshelf_pro_plugins.rest.bookshelf.request;

import java.util.UUID;

public record GetAllBookshelfEntriesRequest(
    UUID accountId,
    String name,
    String password
) {}
