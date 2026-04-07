package org.a1cey.bookshelf_pro_plugins.rest.media_item.review.request;

public record ReviewRequest(
    float rating,
    String comment
) {}
