package org.a1cey.bookshelf_pro_plugins.rest.media_item.request;

import java.util.Set;

import org.a1cey.bookshelf_pro_domain.media_item.MediaItemType;
import org.jspecify.annotations.Nullable;

public record SearchRequest(
    @Nullable String titleFragment,
    @Nullable String subtitleFragment,
    @Nullable Set<String> languages,
    @Nullable MediaItemType mediaItemType,
    @Nullable SpecificSearchRequest specificSearchRequest
) {}

