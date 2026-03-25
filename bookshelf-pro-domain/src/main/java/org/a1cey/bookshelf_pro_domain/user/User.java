package org.a1cey.bookshelf_pro_domain.user;

import org.a1cey.bookshelf_pro_domain.bookshelf_entry.BookshelfEntryID;
import org.a1cey.bookshelf_pro_domain.playlist.PlaylistID;
import org.a1cey.bookshelf_pro_domain.watchlist.WatchlistID;
import org.jmolecules.ddd.annotation.AggregateRoot;
import org.jmolecules.ddd.annotation.Identity;

import java.util.HashSet;
import java.util.Set;

@AggregateRoot
public final class User {

    @Identity
    private final UserID id;
    private final Set<PlaylistID> playlists;
    private final Set<WatchlistID> watchlists;
    private final Set<BookshelfEntryID> bookshelfEntries;

    private User(UserID id, Set<PlaylistID> playlists, Set<WatchlistID> watchlists, Set<BookshelfEntryID> bookshelfEntries) {
        this.id = id;
        this.playlists = playlists;
        this.watchlists = watchlists;
        this.bookshelfEntries = bookshelfEntries;
    }

    public UserID id() {
        return id;
    }

    public Set<PlaylistID> playlists() {
        return playlists;
    }

    public boolean addPlaylist(PlaylistID playlistID) {
        return playlists.add(playlistID);
    }

    public boolean removePlaylist(PlaylistID playlistID) {
        return playlists.remove(playlistID);
    }

    public Set<WatchlistID> watchlists() {
        return watchlists;
    }

    public boolean addWatchlist(WatchlistID watchlistID) {
        return watchlists.add(watchlistID);
    }

    public boolean removeWatchlist(WatchlistID watchlistID) {
        return watchlists.remove(watchlistID);
    }

    public Set<BookshelfEntryID> bookshelfEntries() {
        return bookshelfEntries;
    }

    public boolean addBookshelfEntry(BookshelfEntryID bookshelfEntryID) {
        return bookshelfEntries.add(bookshelfEntryID);
    }

    public boolean removeBookShelfEntry(BookshelfEntryID bookshelfEntryID) {
        return bookshelfEntries.remove(bookshelfEntryID);
    }

    public static UserBuilder builder(UserID id) {
        return new UserBuilder(id);
    }

    public static final class UserBuilder {

        private final UserID id;
        private final Set<PlaylistID> playlists = new HashSet<>();
        private final Set<WatchlistID> watchlists = new HashSet<>();
        private final Set<BookshelfEntryID> bookshelfEntries = new HashSet<>();

        public UserBuilder(UserID id) {
            this.id = id;
        }

        public UserBuilder playlists(Set<PlaylistID> playlists) {
            this.playlists.addAll(playlists);
            return this;
        }

        public UserBuilder playlist(PlaylistID playlistID) {
            this.playlists.add(playlistID);
            return this;
        }

        public UserBuilder watchlists(Set<WatchlistID> watchlists) {
            this.watchlists.addAll(watchlists);
            return this;
        }

        public UserBuilder watchlist(WatchlistID watchlistID) {
            this.watchlists.add(watchlistID);
            return this;
        }

        public UserBuilder bookshelfEntries(Set<BookshelfEntryID> bookshelfEntries) {
            this.bookshelfEntries.addAll(bookshelfEntries);
            return this;
        }

        public UserBuilder bookshelfEntry(BookshelfEntryID bookshelfEntryID) {
            this.bookshelfEntries.add(bookshelfEntryID);
            return this;
        }

        public User build() {
            return new User(id, playlists, watchlists, bookshelfEntries);
        }

    }

}
