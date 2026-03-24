package org.a1cey.bookshelf_pro_domain.user;

import org.a1cey.bookshelf_pro_domain.bookshelf_entry.BookshelfEntryID;
import org.a1cey.bookshelf_pro_domain.playlist.PlaylistID;
import org.a1cey.bookshelf_pro_domain.watchlist.WatchlistID;
import org.jmolecules.ddd.annotation.AggregateRoot;

import java.util.Set;

@AggregateRoot
public class User {

    private final UserID id;
    private Set<PlaylistID> playlists;
    private Set<WatchlistID> watchlists;
    private Set<BookshelfEntryID> bookshelfEntries;

    public User(UserID id, Set<PlaylistID> playlists, Set<WatchlistID> watchlists, Set<BookshelfEntryID> bookshelfEntries) {
        this.id = id;
        this.playlists = playlists;
        this.watchlists = watchlists;
        this.bookshelfEntries = bookshelfEntries;
    }

}
