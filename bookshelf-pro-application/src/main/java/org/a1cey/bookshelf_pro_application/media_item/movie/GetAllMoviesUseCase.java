package org.a1cey.bookshelf_pro_application.media_item.movie;

import java.util.stream.Collectors;

import org.a1cey.bookshelf_pro_application.dto.MovieDto;
import org.a1cey.bookshelf_pro_application.media_item.movie.result.GetAllMoviesResult;
import org.a1cey.bookshelf_pro_domain.media_item.MediaItemRepository;
import org.a1cey.bookshelf_pro_domain.media_item.MediaItemType;
import org.a1cey.bookshelf_pro_domain.media_item.movie.Movie;
import org.springframework.stereotype.Service;

@Service
public final class GetAllMoviesUseCase {
    private final MediaItemRepository mediaItemRepository;

    public GetAllMoviesUseCase(MediaItemRepository mediaItemRepository) {
        this.mediaItemRepository = mediaItemRepository;
    }

    public GetAllMoviesResult execute() {
        var movies = mediaItemRepository
                         .findByType(MediaItemType.MOVIE)
                         .stream()
                         .map(mediaItem -> MovieDto.from((Movie) mediaItem))
                         .collect(Collectors.toSet());

        return new GetAllMoviesResult(movies);
    }
}
