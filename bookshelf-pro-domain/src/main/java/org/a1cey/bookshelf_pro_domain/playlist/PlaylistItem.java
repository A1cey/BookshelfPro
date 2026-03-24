package org.a1cey.bookshelf_pro_domain.playlist;

import org.a1cey.bookshelf_pro_domain.bookshelf_entry.BookshelfEntryID;
import org.jmolecules.ddd.annotation.Entity;

// PlaylistItem needs an ID because duplicates are allowed
@Entity
public record PlaylistItem(
        PlaylistItemID id,
        BookshelfEntryID bookshelfEntryID
        // TODO: Maybe position tracked here?
) {
}
