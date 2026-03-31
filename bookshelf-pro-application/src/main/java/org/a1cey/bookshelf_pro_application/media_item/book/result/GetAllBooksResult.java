package org.a1cey.bookshelf_pro_application.media_item.book.result;

import java.util.Set;

import org.a1cey.bookshelf_pro_application.media_item.book.BookDto;

public record GetAllBooksResult(
    Set<BookDto> books
) {}
