package org.a1cey.bookshelf_pro_plugins.rest.media_item.movie.request;

import java.net.URI;
import java.time.LocalDate;
import java.util.Set;

import org.a1cey.bookshelf_pro_application.dto.ActorDto;
import org.jspecify.annotations.Nullable;

public record UpdateMovieRequest(
    @Nullable String title,
    @Nullable Integer durationSeconds,
    @Nullable String subtitle,
    @Nullable String description,
    @Nullable URI coverImageUrl,
    @Nullable Set<String> languages,
    @Nullable Set<ActorDto> actors,
    @Nullable Set<String> directors,
    @Nullable LocalDate releaseDate,
    @Nullable Set<String> studios,
    @Nullable String originCountry
) {}
