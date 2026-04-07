package org.a1cey.bookshelf_pro_plugins.rest.bookshelf.watchlist.request;

import java.util.List;
import java.util.UUID;

public record CreateWatchlistRequest(
    String title,
    List<UUID> itemsAsBookshelfEntryIds
) {}
