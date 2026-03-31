package org.a1cey.bookshelf_pro_plugins.rest.account.request;

public record GetAccountRequest(
    String name,
    String password
) {}
