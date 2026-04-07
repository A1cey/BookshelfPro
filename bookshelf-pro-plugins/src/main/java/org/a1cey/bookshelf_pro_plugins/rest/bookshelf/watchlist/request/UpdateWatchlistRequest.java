package org.a1cey.bookshelf_pro_plugins.rest.bookshelf.watchlist.request;

import java.util.List;
import java.util.UUID;

import org.jspecify.annotations.Nullable;

public record UpdateWatchlistRequest(
    @Nullable String newTitle,
    @Nullable List<UUID> newItemsAsBookshelfEntryIds,
    @Nullable List<UUID> removeItemsAsBookshelfEntryIds
) {}
