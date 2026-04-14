package org.a1cey.bookshelf_pro_application.dto;

import java.net.URI;
import java.util.Set;
import java.util.UUID;

import org.jspecify.annotations.Nullable;

public sealed interface MediaItemDto permits BookDto, MovieDto {
    UUID mediaItemId();

    UUID ownerId();

    String title();

    String subtitle();

    @Nullable URI coverImageUrl();

    String description();

    Set<String> languages();
}
