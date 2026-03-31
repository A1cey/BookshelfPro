package org.a1cey.bookshelf_pro_plugins.rest.account.request;

import java.util.UUID;

public record DeleteAccountRequest(
    UUID accountId,
    String name,
    String password
) {}
