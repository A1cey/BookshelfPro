package org.a1cey.bookshelf_pro_domain.media_item;

import org.a1cey.bookshelf_pro_domain.label.LabelID;

import java.util.Optional;
import java.util.Set;

public record MediaItemSearchCriteria(
        Optional<String> titleFragment,
        Optional<Set<LabelID>> labels,
        Optional<MediaItemType> mediaItemType,
        Optional<SpecificSearchCriteria> typeCriteria
) {}

