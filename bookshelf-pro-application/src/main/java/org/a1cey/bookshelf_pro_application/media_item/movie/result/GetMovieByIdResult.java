package org.a1cey.bookshelf_pro_application.media_item.movie.result;

import org.a1cey.bookshelf_pro_application.dto.MovieDto;

public record GetMovieByIdResult(
    MovieDto movie
) {}
