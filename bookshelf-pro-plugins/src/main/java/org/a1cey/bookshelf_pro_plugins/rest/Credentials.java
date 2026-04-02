package org.a1cey.bookshelf_pro_plugins.rest;

import java.util.UUID;

public record Credentials(
    UUID accountId,
    String username,
    String password
) {
}
