package org.a1cey.bookshelf_pro_application.media_item;

import java.util.stream.Collectors;

import org.a1cey.bookshelf_pro_application.dto.BookDto;
import org.a1cey.bookshelf_pro_application.dto.MovieDto;
import org.a1cey.bookshelf_pro_application.media_item.result.GetAllMediaItemsResult;
import org.a1cey.bookshelf_pro_domain.media_item.MediaItemRepository;
import org.a1cey.bookshelf_pro_domain.media_item.MediaItemType;
import org.a1cey.bookshelf_pro_domain.media_item.book.Book;
import org.a1cey.bookshelf_pro_domain.media_item.movie.Movie;
import org.springframework.stereotype.Service;

@Service
public class GetAllMediaItemsUseCase {
    private final MediaItemRepository mediaItemRepository;

    public GetAllMediaItemsUseCase(MediaItemRepository mediaItemRepository) {
        this.mediaItemRepository = mediaItemRepository;
    }

    public GetAllMediaItemsResult execute() {
        var books = mediaItemRepository
                        .findByType(MediaItemType.BOOK)
                        .stream()
                        .map(book -> BookDto.from((Book) book))
                        .collect(Collectors.toSet());
        var movies = mediaItemRepository
                         .findByType(MediaItemType.MOVIE)
                         .stream()
                         .map(movie -> MovieDto.from((Movie) movie))
                         .collect(Collectors.toSet());

        return new GetAllMediaItemsResult(books, movies);
    }
}
