package org.a1cey.bookshelf_pro_application.bookshelf.result;

import java.util.Set;

import org.a1cey.bookshelf_pro_application.dto.BookshelfEntryDto;

public record GetAllBookshelfEntriesResult(Set<BookshelfEntryDto> bookshelfEntries) {}
