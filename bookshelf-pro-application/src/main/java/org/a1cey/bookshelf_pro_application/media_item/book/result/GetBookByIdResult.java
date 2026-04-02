package org.a1cey.bookshelf_pro_application.media_item.book.result;

import org.a1cey.bookshelf_pro_application.dto.BookDto;

public record GetBookByIdResult(
    BookDto book
) {}
