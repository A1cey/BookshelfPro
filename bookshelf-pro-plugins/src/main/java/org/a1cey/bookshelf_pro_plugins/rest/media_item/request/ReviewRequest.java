package org.a1cey.bookshelf_pro_plugins.rest.media_item.request;

import java.util.UUID;

public record ReviewRequest(
    UUID accountId,
    String name,
    String password,
    float rating,
    String comment
) {}
