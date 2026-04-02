package org.a1cey.bookshelf_pro_domain.bookshelf.playlist;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.a1cey.bookshelf_pro_domain.OwnershipPolicy;
import org.a1cey.bookshelf_pro_domain.Title;
import org.a1cey.bookshelf_pro_domain.account.AccountId;
import org.jmolecules.ddd.annotation.AggregateRoot;
import org.jmolecules.ddd.annotation.Identity;

import jakarta.validation.Valid;
import jakarta.validation.constraints.PositiveOrZero;

@AggregateRoot
public final class Playlist {

    @Identity
    private final PlaylistId id;
    private final AccountId owner;
    private final LinkedList<PlaylistItem> items; // LinkedList is best for inserting/removing
    @Valid
    private Title title;

    public Playlist(PlaylistId id, AccountId owner, Title title, List<PlaylistItem> items) {
        this.id = id;
        this.owner = owner;
        this.title = title;
        this.items = new LinkedList<>(items);
    }

    public PlaylistId id() {
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

    public List<PlaylistItem> items() {
        return Collections.unmodifiableList(items);
    }

    public void addItem(PlaylistItem item, AccountId userRequestingChange) {
        OwnershipPolicy.validate(owner, userRequestingChange, id);
        items.addLast(item);
    }

    public void moveItem(@PositiveOrZero int oldPosition, @PositiveOrZero int newPosition, AccountId userRequestingChange) {
        OwnershipPolicy.validate(owner, userRequestingChange, id);

        if (oldPosition < 0) {
            throw new IllegalArgumentException("oldPosition (" + oldPosition + ") is less than 0.");
        }
        if (oldPosition >= items.size()) {
            throw new IllegalArgumentException(
                "oldPosition (" + oldPosition + ") is greater than length of playlist (" + items.size() + " items)."
            );
        }
        if (newPosition < 0) {
            throw new IllegalArgumentException("oldPosition (" + oldPosition + ")  is less than 0.");
        }
        if (newPosition >= items.size()) {
            throw new IllegalArgumentException(
                "oldPosition (" + oldPosition + ")  is greater than length of playlist (" + items.size() + " items)."
            );
        }

        var item = items.remove(oldPosition);
        items.add(newPosition, item);
    }

    public boolean removeItem(PlaylistItemId itemId, AccountId userRequestingChange) {
        OwnershipPolicy.validate(owner, userRequestingChange, id);
        return items.removeIf(item -> item.id().equals(itemId));
    }

}
