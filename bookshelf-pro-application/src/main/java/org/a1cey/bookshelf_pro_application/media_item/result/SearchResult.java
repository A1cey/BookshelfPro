package org.a1cey.bookshelf_pro_application.media_item.result;

import java.util.Set;

import org.a1cey.bookshelf_pro_application.dto.MediaItemDto;

public record SearchResult(Set<? extends MediaItemDto> mediaItems) {}
