package org.a1cey.bookshelf_pro_plugins.rest.bookshelf.playlist.request;

import java.util.List;
import java.util.Set;

import org.a1cey.bookshelf_pro_application.dto.MovePlaylistItemDto;
import org.a1cey.bookshelf_pro_application.dto.NewPlaylistItemDto;
import org.jspecify.annotations.Nullable;

public record UpdatePlaylistRequest(
    @Nullable String newTitle,
    @Nullable List<NewPlaylistItemDto> newItems,
    @Nullable Set<Integer> removeItemsFromPositions,
    @Nullable Set<MovePlaylistItemDto> moveItems
) {}
