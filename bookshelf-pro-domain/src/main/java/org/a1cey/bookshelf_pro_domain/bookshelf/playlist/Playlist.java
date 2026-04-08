package org.a1cey.bookshelf_pro_domain.bookshelf.playlist;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.a1cey.bookshelf_pro_domain.OwnershipPolicy;
import org.a1cey.bookshelf_pro_domain.Title;
import org.a1cey.bookshelf_pro_domain.account.AccountId;
import org.a1cey.bookshelf_pro_domain.bookshelf.bookshelf_entry.BookshelfEntryId;
import org.jmolecules.ddd.annotation.AggregateRoot;
import org.jmolecules.ddd.annotation.Identity;

import jakarta.validation.Valid;

@AggregateRoot
public final class Playlist {

    @Identity
    private final PlaylistId id;
    private final AccountId owner;
    private final List<BookshelfEntryId> items;
    @Valid
    private Title title;

    public Playlist(PlaylistId id, AccountId owner, Title title, List<BookshelfEntryId> items) {
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

    public List<BookshelfEntryId> items() {
        return Collections.unmodifiableList(items);
    }

    public void addItem(NewPlaylistItem newPlaylistItem, AccountId userRequestingChange) {
        OwnershipPolicy.validate(owner, userRequestingChange, id);

        if (newPlaylistItem.position().isPresent()) {
            if (newPlaylistItem.position().get().value() > items.size()) {
                throw new IllegalArgumentException(
                    "position (" + newPlaylistItem.position() + ") is greater than length of playlist (" + items.size() + ")."
                );
            }
            items.add(newPlaylistItem.position().get().value(), newPlaylistItem.item());
        } else {
            items.addLast(newPlaylistItem.item());
        }
    }

    public void moveItem(MovePlayListItem movePlayListItem, AccountId userRequestingChange) {
        OwnershipPolicy.validate(owner, userRequestingChange, id);

        if (movePlayListItem.oldPosition().value() >= items.size()) {
            throw new IllegalArgumentException(
                "oldPosition (" + movePlayListItem.oldPosition() + ") is greater than length of playlist (" + items.size() + ")."
            );
        }
        if (movePlayListItem.newPosition().value() >= items.size()) {
            throw new IllegalArgumentException(
                "newPosition (" + movePlayListItem.newPosition() + ")  is greater than length of playlist (" + items.size() + ")."
            );
        }

        var item = items.remove(movePlayListItem.oldPosition().value());
        items.add(movePlayListItem.newPosition().value(), item);
    }

    public void removeItem(PlaylistPosition position, AccountId userRequestingChange) {
        OwnershipPolicy.validate(owner, userRequestingChange, id);

        if (position.value() >= items.size()) {
            throw new IllegalArgumentException(
                "position (" + position + ")  is greater than length of playlist (" + items.size() + ")."
            );
        }

        items.remove(position.value());
    }

}
