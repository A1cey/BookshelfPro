package org.a1cey.bookshelf_pro_domain.watchlist;

import org.a1cey.bookshelf_pro_domain.bookshelf_entry.BookshelfEntry;
import org.a1cey.bookshelf_pro_domain.bookshelf_entry.BookshelfEntryID;
import org.a1cey.bookshelf_pro_domain.bookshelf_entry.consumption.ConsumptionState;
import org.jmolecules.ddd.annotation.ValueObject;

@ValueObject
public final class WatchlistItem {

    private final BookshelfEntryID bookshelfEntryID;

    private WatchlistItem(BookshelfEntryID id) {
        bookshelfEntryID = id;
    }

    public static WatchlistItem of(BookshelfEntry bookshelfEntry) {
        if (bookshelfEntry.consumptionProgress().state() == ConsumptionState.COMPLETED) {
            // TODO: This should have the name
            throw new IllegalArgumentException(
                    "Cannot add completed media item'" + bookshelfEntry.mediaItemID() + "' to a watchlist."
            );
        }
        return new WatchlistItem(bookshelfEntry.id());
    }

    BookshelfEntryID bookshelfEntryID() {
        return bookshelfEntryID;
    }

}
