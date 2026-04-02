package org.a1cey.bookshelf_pro_application.bookshelf.result;

import java.util.Set;

import org.a1cey.bookshelf_pro_domain.bookshelf.bookshelf_entry.BookshelfEntry;

public record GetAllBookshelfEntriesResult(Set<BookshelfEntry> bookshelfEntries) {}
