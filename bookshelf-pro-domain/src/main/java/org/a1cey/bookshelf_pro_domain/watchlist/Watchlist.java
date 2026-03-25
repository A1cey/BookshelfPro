package org.a1cey.bookshelf_pro_domain.watchlist;

import org.a1cey.bookshelf_pro_domain.Title;
import org.jmolecules.ddd.annotation.AggregateRoot;
import org.jmolecules.ddd.annotation.Identity;

import java.util.LinkedHashSet;
import java.util.SequencedSet;

@AggregateRoot
public class Watchlist {

    @Identity
    private final WatchlistID id;
    private Title title;
    private final LinkedHashSet<WatchlistItem> items; // TODO: Watchlists are not unsorted, but defined by insertion order, they cannot
    // be rearranged

    public Watchlist(Title title, WatchlistID id, SequencedSet<WatchlistItem> items) {
        this.title = title;
        this.id = id;
        this.items = new LinkedHashSet<>(items);
    }

    public WatchlistID id() {
        return id;
    }

    public Title title() {
        return title;
    }

    public void changeTitle(Title newTitle) {
        title = newTitle;
    }

    public SequencedSet<WatchlistItem> items() {
        return items;
    }

    public boolean addItem(WatchlistItem item) {
        return items.add(item);
    }

    public boolean removeItem(WatchlistItem item) {
        return items.remove(item);
    }

}
