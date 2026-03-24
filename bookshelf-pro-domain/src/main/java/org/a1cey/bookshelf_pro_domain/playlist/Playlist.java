package org.a1cey.bookshelf_pro_domain.playlist;

import jakarta.validation.constraints.PositiveOrZero;
import org.a1cey.bookshelf_pro_domain.Title;
import org.jmolecules.ddd.annotation.Entity;

import java.util.LinkedList;
import java.util.List;

@Entity
public class Playlist {

    private final PlaylistID id;
    private Title title;
    private final LinkedList<PlaylistItem> items; // LinkedList is best for inserting/removing

    public Playlist(Title title, PlaylistID id, List<PlaylistItem> items) {
        this.title = title;
        this.id = id;
        this.items = new LinkedList<>(items);
    }

    public PlaylistID id() {
        return id;
    }

    public Title title() {
        return title;
    }

    public void changeTitle(Title newTitle) {
        title = newTitle;
    }

    public List<PlaylistItem> items() {
        return items;
    }

    public void addItem(PlaylistItem item) {
        items.addLast(item);
    }

    public void moveItem(@PositiveOrZero int oldPosition, @PositiveOrZero int newPosition) {
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

    public boolean removeItem(PlaylistItemID itemId) {
        return items.removeIf(item -> item.id().equals(itemId));
    }

}
