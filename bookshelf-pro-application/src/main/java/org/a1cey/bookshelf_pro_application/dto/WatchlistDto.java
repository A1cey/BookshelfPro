package org.a1cey.bookshelf_pro_application.dto;

import java.util.List;
import java.util.UUID;

import org.a1cey.bookshelf_pro_domain.bookshelf.bookshelf_entry.BookshelfEntryId;
import org.a1cey.bookshelf_pro_domain.bookshelf.watchlist.Watchlist;

public record WatchlistDto(
    UUID watchlistId,
    UUID owner,
    String title,
    List<UUID> itemsAsBookshelfEntryIds
) {

    public static WatchlistDto from(Watchlist watchlist) {
        return new WatchlistDto(
            watchlist.id().value(),
            watchlist.owner().value(),
            watchlist.title().title(),
            watchlist.items().stream().map(BookshelfEntryId::value).toList()
        );
    }
}
