package org.a1cey.bookshelf_pro_domain.bookshelf.playlist;

import java.util.Optional;

import org.a1cey.bookshelf_pro_domain.bookshelf.bookshelf_entry.BookshelfEntryId;
import org.jmolecules.ddd.annotation.ValueObject;

@ValueObject
public record NewPlaylistItem(BookshelfEntryId item, Optional<PlaylistPosition> position) {}
