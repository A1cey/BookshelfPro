package org.a1cey.bookshelf_pro_domain.media_item;

import java.util.Optional;

public record MediaItemSearchCriteria(
    Optional<String> titleFragment,
    Optional<MediaItemType> mediaItemType,
    Optional<SpecificSearchCriteria> typeCriteria
) {}

