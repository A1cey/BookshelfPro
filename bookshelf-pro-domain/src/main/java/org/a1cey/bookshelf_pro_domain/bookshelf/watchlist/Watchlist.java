package org.a1cey.bookshelf_pro_domain.bookshelf.watchlist;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.SequencedSet;

import org.a1cey.bookshelf_pro_domain.OwnershipPolicy;
import org.a1cey.bookshelf_pro_domain.Title;
import org.a1cey.bookshelf_pro_domain.account.AccountId;
import org.a1cey.bookshelf_pro_domain.bookshelf.bookshelf_entry.BookshelfEntryId;
import org.jmolecules.ddd.annotation.AggregateRoot;
import org.jmolecules.ddd.annotation.Identity;

@AggregateRoot
public final class Watchlist {

    @Identity
    private final WatchlistId id;
    private final AccountId owner;
    // TODO: Watchlists are not unsorted, but defined by insertion order, they cannot be rearranged
    private final LinkedHashSet<BookshelfEntryId> items;
    private Title title;

    public Watchlist(WatchlistId id, AccountId owner, Title title, SequencedSet<BookshelfEntryId> items) {
        this.id = id;
        this.owner = owner;
        this.title = title;
        this.items = new LinkedHashSet<>(items);
    }

    public WatchlistId id() {
        return id;
    }

    public AccountId owner() {
        return owner;
    }

    public Title title() {
        return title;
    }

    public void changeTitle(Title newTitle, AccountId userRequestingChange) {
        OwnershipPolicy.validate(owner, userRequestingChange, id);
        title = newTitle;
    }

    public SequencedSet<BookshelfEntryId> items() {
        return Collections.unmodifiableSequencedSet(items);
    }

    public void changeItems(SequencedSet<BookshelfEntryId> newItems, AccountId userRequestingChange) {
        OwnershipPolicy.validate(owner, userRequestingChange, id);
        items.clear();
        items.addAll(newItems);
    }

    public boolean addItem(BookshelfEntryId item, AccountId userRequestingChange) {
        OwnershipPolicy.validate(owner, userRequestingChange, id);
        return items.add(item);
    }

    public boolean removeItem(BookshelfEntryId item, AccountId userRequestingChange) {
        OwnershipPolicy.validate(owner, userRequestingChange, id);
        return items.remove(item);
    }

}
