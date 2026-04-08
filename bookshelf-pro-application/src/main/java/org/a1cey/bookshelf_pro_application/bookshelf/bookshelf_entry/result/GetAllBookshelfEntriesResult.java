package org.a1cey.bookshelf_pro_application.bookshelf.bookshelf_entry.result;

import java.util.Set;

import org.a1cey.bookshelf_pro_application.dto.BookshelfEntryDto;

public record GetAllBookshelfEntriesResult(Set<BookshelfEntryDto> bookshelfEntries) {}
