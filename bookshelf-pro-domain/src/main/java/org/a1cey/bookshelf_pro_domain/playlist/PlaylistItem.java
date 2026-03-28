package org.a1cey.bookshelf_pro_domain.playlist;

import org.a1cey.bookshelf_pro_domain.bookshelf_entry.BookshelfEntryId;
import org.jmolecules.ddd.annotation.Entity;

// TODO: Maybe position tracked here?
// PlaylistItem needs an ID because duplicates are allowed
@Entity
public record PlaylistItem(
    PlaylistItemId id,
    BookshelfEntryId bookshelfEntryId
) {}
