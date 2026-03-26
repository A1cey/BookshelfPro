package org.a1cey.bookshelf_pro_domain.watchlist;

import org.a1cey.bookshelf_pro_domain.OwnershipPolicy;
import org.a1cey.bookshelf_pro_domain.Title;
import org.a1cey.bookshelf_pro_domain.user.UserID;
import org.jmolecules.ddd.annotation.AggregateRoot;
import org.jmolecules.ddd.annotation.Identity;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.SequencedSet;

@AggregateRoot
public final class Watchlist {

    @Identity
    private final WatchlistID id;
    private final UserID owner;
    private Title title;
    private final LinkedHashSet<WatchlistItem> items; // TODO: Watchlists are not unsorted, but defined by insertion order, they cannot
    // be rearranged

    public Watchlist(WatchlistID id, UserID owner, Title title, SequencedSet<WatchlistItem> items) {
        this.id = id;
        this.owner = owner;
        this.title = title;
        this.items = new LinkedHashSet<>(items);
    }

    public WatchlistID id() {
        return id;
    }

    public UserID owner() {
        return owner;
    }

    public Title title() {
        return title;
    }

    public void changeTitle(Title newTitle, UserID userRequestingChange) {
        OwnershipPolicy.validate(owner, userRequestingChange, id.id());
        title = newTitle;
    }

    public SequencedSet<WatchlistItem> items() {
        return Collections.unmodifiableSequencedSet(items);
    }

    public boolean addItem(WatchlistItem item, UserID userRequestingChange) {
        OwnershipPolicy.validate(owner, userRequestingChange, id.id());
        return items.add(item);
    }

    public boolean removeItem(WatchlistItem item, UserID userRequestingChange) {
        OwnershipPolicy.validate(owner, userRequestingChange, id.id());
        return items.remove(item);
    }

}
