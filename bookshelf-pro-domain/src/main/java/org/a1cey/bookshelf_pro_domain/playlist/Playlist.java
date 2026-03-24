package org.a1cey.bookshelf_pro_domain.playlist;

//Nutzer können eigene Sammlungen in Form von Watchlists und Playlists erstellen. Diese können benannt werden.
//Watchlists sind unsortierte Sammlungen von Medien, welcher der Nutzer in Zukunft konsumieren
//möchte. Eine Watchlist darf kein Medium enthalten, das bereits den Status ABGESCHLOSSEN hat. Das Hinzufügen eines solchen Items wird
//abgelehnt. Ein Medium darf in einer Watchlist nur einmal vorkommen.
//Eine Playlist ist eine sortierte Sammlung an Medien. Die Reihenfolge ist durch die Einfügeposition bestimmt, kann aber
//vom Nutzer geändert werden. Medien dürfen mehrmals in einer Playlist vorkommen.

import org.a1cey.bookshelf_pro_domain.Title;
import org.a1cey.bookshelf_pro_domain.media_item.MediaItemID;
import org.jmolecules.ddd.annotation.Entity;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Playlist {

    private final PlaylistID id;
    private Title title;
    private final ArrayList<MediaItemID> items;

    public Playlist(Title title, PlaylistID id, List<MediaItemID> items) {
        this.title = title;
        this.id = id;
        this.items = new ArrayList<>(items);
    }

    public PlaylistID id() {
        return id;
    }

    public Title title() {
        return title;
    }

    public List<MediaItemID> items() {
        return items;
    }

}
