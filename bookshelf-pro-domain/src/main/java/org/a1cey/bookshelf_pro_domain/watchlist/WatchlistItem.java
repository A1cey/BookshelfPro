package org.a1cey.bookshelf_pro_domain.watchlist;

import org.a1cey.bookshelf_pro_domain.bookshelf_entry.BookshelfEntry;
import org.a1cey.bookshelf_pro_domain.bookshelf_entry.BookshelfEntryID;
import org.a1cey.bookshelf_pro_domain.consumption.ConsumptionState;
import org.jmolecules.ddd.annotation.ValueObject;

@ValueObject
public record WatchlistItem(BookshelfEntryID bookshelfEntryID) {

    private WatchlistItem {}

    public static WatchlistItem of(BookshelfEntry bookshelfEntry) {
        if (bookshelfEntry.consumptionProgress().getState() == ConsumptionState.COMPLETED) {
            // TODO: This should have the name
            throw new IllegalArgumentException(
                    "Cannot add completed media item'" + bookshelfEntry.mediaItemID() + "' to a watchlist."
            );
        }
        return new WatchlistItem(bookshelfEntry.id());
    }

}
