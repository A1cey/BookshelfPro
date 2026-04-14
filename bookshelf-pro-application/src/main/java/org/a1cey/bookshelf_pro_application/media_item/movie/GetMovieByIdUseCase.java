package org.a1cey.bookshelf_pro_application.media_item.movie;

import java.util.Optional;

import org.a1cey.bookshelf_pro_application.dto.MovieDto;
import org.a1cey.bookshelf_pro_application.media_item.movie.command.GetMovieCommand;
import org.a1cey.bookshelf_pro_application.media_item.movie.result.GetMovieByIdResult;
import org.a1cey.bookshelf_pro_domain.media_item.MediaItemRepository;
import org.a1cey.bookshelf_pro_domain.media_item.MediaItemType;
import org.a1cey.bookshelf_pro_domain.media_item.movie.Movie;
import org.springframework.stereotype.Service;

@Service
public final class GetMovieByIdUseCase {
    private final MediaItemRepository mediaItemRepository;

    public GetMovieByIdUseCase(MediaItemRepository mediaItemRepository) {
        this.mediaItemRepository = mediaItemRepository;
    }

    public Optional<GetMovieByIdResult> execute(GetMovieCommand command) {
        var movie = mediaItemRepository.findById(command.movieId()).map(mediaItem -> {
            if (mediaItem.type() != MediaItemType.MOVIE) {
                throw new IllegalArgumentException("Id " + command.movieId() + " exists but is not a movie but a " + mediaItem.type());
            }

            return (Movie) mediaItem;
        });

        return movie.map(m -> new GetMovieByIdResult(MovieDto.from(m)));
    }
}
