package org.a1cey.bookshelf_pro_application.media_item.movie.result;

import java.util.Set;

import org.a1cey.bookshelf_pro_application.dto.MovieDto;

public record GetAllMoviesResult(
    Set<MovieDto> movies
) {}
