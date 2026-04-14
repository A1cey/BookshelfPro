package org.a1cey.bookshelf_pro_application.media_item.movie.command;

import java.net.URI;
import java.util.Optional;
import java.util.Set;

import org.a1cey.bookshelf_pro_domain.Title;
import org.a1cey.bookshelf_pro_domain.media_item.Description;
import org.a1cey.bookshelf_pro_domain.media_item.Language;
import org.a1cey.bookshelf_pro_domain.media_item.Subtitle;
import org.a1cey.bookshelf_pro_domain.media_item.movie.Actor;
import org.a1cey.bookshelf_pro_domain.media_item.movie.Director;
import org.a1cey.bookshelf_pro_domain.media_item.movie.Duration;
import org.a1cey.bookshelf_pro_domain.media_item.movie.ImdbTitleId;
import org.a1cey.bookshelf_pro_domain.media_item.movie.OriginCountry;
import org.a1cey.bookshelf_pro_domain.media_item.movie.ReleaseDate;
import org.a1cey.bookshelf_pro_domain.media_item.movie.Studio;

public record CreateMovieCommand(
    Title title,
    Optional<Subtitle> subtitle,
    Optional<Description> description,
    Optional<URI> coverImageUrl,
    Optional<Set<Language>> languages,
    ImdbTitleId imdbTitleId,
    Optional<Set<Actor>> actors,
    Optional<Set<Director>> directors,
    Optional<ReleaseDate> releaseDate,
    Optional<Set<Studio>> studios,
    Optional<OriginCountry> originCountry,
    Duration duration
) {}
