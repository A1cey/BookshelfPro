package org.a1cey.bookshelf_pro_domain.media_item;

import java.util.Optional;
import java.util.Set;

public record MediaItemSearchCriteria(
    Optional<String> titleFragment,
    Optional<Subtitle> subtitleFragment,
    Optional<Set<Language>> languages,
    Optional<MediaItemType> mediaItemType,
    Optional<SpecificSearchCriteria> typeCriteria
) {}

