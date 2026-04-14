package org.a1cey.bookshelf_pro_application.media_item.result;

import java.util.Set;

import org.a1cey.bookshelf_pro_application.dto.BookDto;
import org.a1cey.bookshelf_pro_application.dto.MovieDto;

public record GetAllMediaItemsResult(
    Set<BookDto> books,
    Set<MovieDto> movies
) {}
