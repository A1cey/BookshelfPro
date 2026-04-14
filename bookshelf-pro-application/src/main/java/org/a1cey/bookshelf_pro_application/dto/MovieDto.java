package org.a1cey.bookshelf_pro_application.dto;

import java.net.URI;
import java.time.Duration;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.a1cey.bookshelf_pro_domain.media_item.Language;
import org.a1cey.bookshelf_pro_domain.media_item.movie.Director;
import org.a1cey.bookshelf_pro_domain.media_item.movie.Movie;
import org.a1cey.bookshelf_pro_domain.media_item.movie.Studio;
import org.jspecify.annotations.Nullable;

public record MovieDto(
    UUID mediaItemId,
    UUID ownerId,
    String title,
    String subtitle,
    @Nullable URI coverImageUrl,
    String description,
    Set<String> languages,
    String imdbTitleId,
    Set<ActorDto> actors,
    Set<String> directors,
    @Nullable
    LocalDate releaseDate,
    Set<String> studios,
    String originCountry,
    Duration duration
) implements MediaItemDto {

    public static MovieDto from(Movie movie) {
        return new MovieDto(
            movie.id().value(),
            movie.owner().value(),
            movie.title().title(),
            movie.subtitle().subtitle(),
            movie.coverImageUrl(),
            movie.description().description(),
            movie.languages().stream().map(Language::isoCode).collect(Collectors.toSet()),
            movie.imdbTitleId().value(),
            movie.actors().stream().map(ActorDto::from).collect(Collectors.toSet()),
            movie.directors().stream().map(Director::name).collect(Collectors.toSet()),
            movie.releaseDate() != null ? movie.releaseDate().date() : null,
            movie.studios().stream().map(Studio::name).collect(Collectors.toSet()),
            movie.originCountry().country(),
            movie.duration().time()
        );
    }

}
