package org.a1cey.bookshelf_pro_domain.watchlist;

//Nutzer können eigene Sammlungen in Form von Watchlists und Playlists erstellen. Diese können benannt werden.
//Watchlists sind unsortierte Sammlungen von Medien, welcher der Nutzer in Zukunft konsumieren
//möchte. Eine Watchlist darf kein Medium enthalten, das bereits den Status ABGESCHLOSSEN hat. Das Hinzufügen eines solchen Items wird
//abgelehnt. Ein Medium darf in einer Watchlist nur einmal vorkommen.
//Eine Playlist ist eine sortierte Sammlung an Medien. Die Reihenfolge ist durch die Einfügeposition bestimmt, kann aber
//vom Nutzer geändert werden. Medien dürfen mehrmals in einer Playlist vorkommen.

import org.a1cey.bookshelf_pro_domain.Title;
import org.jmolecules.ddd.annotation.AggregateRoot;

import java.util.LinkedHashSet;
import java.util.SequencedSet;

@AggregateRoot
public class Watchlist {

    private final WatchlistID id;
    private Title title;
    private final LinkedHashSet<WatchlistItem> items;

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

}
