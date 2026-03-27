package org.a1cey.bookshelf_pro_domain.playlist;

import jakarta.validation.Valid;
import jakarta.validation.constraints.PositiveOrZero;
import org.a1cey.bookshelf_pro_domain.OwnershipPolicy;
import org.a1cey.bookshelf_pro_domain.Title;
import org.a1cey.bookshelf_pro_domain.account.AccountID;
import org.jmolecules.ddd.annotation.AggregateRoot;
import org.jmolecules.ddd.annotation.Identity;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@AggregateRoot
public final class Playlist {

    @Identity
    private final PlaylistID id;
    private final AccountID owner;
    @Valid
    private Title title;
    private final LinkedList<PlaylistItem> items; // LinkedList is best for inserting/removing

    public Playlist(PlaylistID id, AccountID owner, Title title, List<PlaylistItem> items) {
        this.id = id;
        this.owner = owner;
        this.title = title;
        this.items = new LinkedList<>(items);
    }

    public PlaylistID id() {
        return id;
    }

    public AccountID owner() {
        return owner;
    }

    public Title title() {
        return title;
    }

    public void changeTitle(Title newTitle, AccountID userRequestingChange) {
        OwnershipPolicy.validate(owner, userRequestingChange, id);
        title = newTitle;
    }

    public List<PlaylistItem> items() {
        return Collections.unmodifiableList(items);
    }

    public void addItem(PlaylistItem item, AccountID userRequestingChange) {
        OwnershipPolicy.validate(owner, userRequestingChange, id);
        items.addLast(item);
    }

    public void moveItem(@PositiveOrZero int oldPosition, @PositiveOrZero int newPosition, AccountID userRequestingChange) {
        OwnershipPolicy.validate(owner, userRequestingChange, id);

        if (oldPosition < 0) {
            throw new IllegalArgumentException("oldPosition (" + oldPosition + ") is less than 0.");
        }
        if (oldPosition >= items.size()) {
            throw new IllegalArgumentException("oldPosition (" + oldPosition + ") is greater than length of playlist (" + items.size() +
                                                       " items).");
        }
        if (newPosition < 0) {
            throw new IllegalArgumentException("oldPosition (" + oldPosition + ")  is less than 0.");
        }
        if (newPosition >= items.size()) {
            throw new IllegalArgumentException("oldPosition (" + oldPosition + ")  is greater than length of playlist (" + items.size() + " items).");
        }

        var item = items.remove(oldPosition);
        items.add(newPosition, item);
    }

    public boolean removeItem(PlaylistItemID itemId, AccountID userRequestingChange) {
        OwnershipPolicy.validate(owner, userRequestingChange, id);
        return items.removeIf(item -> item.id().equals(itemId));
    }

}
