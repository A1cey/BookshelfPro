package org.a1cey.bookshelf_pro_plugins.rest.media_item.request;

import org.jspecify.annotations.Nullable;

public record UpdateReviewRequest(@Nullable Float rating, @Nullable String comment) {}
