package org.a1cey.bookshelf_pro_domain.watchlist;

//Nutzer können eigene Sammlungen in Form von Watchlists und Playlists erstellen. Diese können benannt werden.
//Watchlists sind unsortierte Sammlungen von Medien, welcher der Nutzer in Zukunft konsumieren
//möchte. Eine Watchlist darf kein Medium enthalten, das bereits den Status ABGESCHLOSSEN hat. Das Hinzufügen eines solchen Items wird
//abgelehnt. Ein Medium darf in einer Watchlist nur einmal vorkommen.
//Eine Playlist ist eine sortierte Sammlung an Medien. Die Reihenfolge ist durch die Einfügeposition bestimmt, kann aber
//vom Nutzer geändert werden. Medien dürfen mehrmals in einer Playlist vorkommen.

import org.a1cey.bookshelf_pro_domain.Title;
import org.a1cey.bookshelf_pro_domain.media_item.MediaItemID;
import org.jmolecules.ddd.annotation.AggregateRoot;

import java.util.LinkedHashSet;
import java.util.SequencedSet;

@AggregateRoot
public class Watchlist {

    private final WatchlistID id;
    private Title title;
    private final LinkedHashSet<WatchlistItem> items;

    public Watchlist(Title title, WatchlistID id, SequencedSet<MediaItemID> items) {
        this.title = title;
        this.id = id;
        this.items = new LinkedHashSet<>(items); // TODO: Check that this set does not contain items which are already completed
    }

    public WatchlistID getId() {
        return id;
    }

    public Title getTitle() {
        return title;
    }

    public SequencedSet<MediaItemID> getItems() {
        return items;
    }

}
