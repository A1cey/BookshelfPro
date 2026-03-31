package org.a1cey.bookshelf_pro_plugins.rest.account.request;

import org.jspecify.annotations.Nullable;

public record UpdateAccountRequest(
    String name,
    String password, // TODO: This is unsecure
    @Nullable String newName,
    @Nullable String newPassword,
    @Nullable String newEmail
) {}
