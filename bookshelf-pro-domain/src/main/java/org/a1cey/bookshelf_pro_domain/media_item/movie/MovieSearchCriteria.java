package org.a1cey.bookshelf_pro_domain.media_item.movie;

import java.util.Optional;
import java.util.Set;

import org.a1cey.bookshelf_pro_domain.media_item.SpecificSearchCriteria;

public record MovieSearchCriteria(
    Optional<ImdbTitleId> imdbTitleId,
    Optional<Set<Actor>> actors,
    Optional<Set<Director>> directors,
    Optional<ReleaseDate> releaseDate,
    Optional<Set<Studio>> studios,
    Optional<OriginCountry> originCountry,
    Optional<Duration> duration
) implements SpecificSearchCriteria {}
