package org.a1cey.bookshelf_pro_plugins.rest.media_item.request;

import java.time.LocalDate;
import java.util.Set;

import org.a1cey.bookshelf_pro_application.dto.ActorDto;
import org.a1cey.bookshelf_pro_domain.media_item.movie.Duration;
import org.jspecify.annotations.Nullable;

public record MovieSearchRequest(
    @Nullable String imdbTitleId,
    @Nullable Set<ActorDto> actors,
    @Nullable Set<String> directors,
    @Nullable LocalDate releaseDate,
    @Nullable Set<String> studios,
    @Nullable String originCountry,
    @Nullable Duration duration
) implements SpecificSearchRequest {}
