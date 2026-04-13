package org.a1cey.bookshelf_pro_application.bookshelf.playlist.command;

import java.util.List;

import org.a1cey.bookshelf_pro_domain.Title;
import org.a1cey.bookshelf_pro_domain.bookshelf.bookshelf_entry.BookshelfEntryId;

public record CreatePlaylistCommand(Title title, List<BookshelfEntryId> itemsAsBookshelfEntryIds) {}
