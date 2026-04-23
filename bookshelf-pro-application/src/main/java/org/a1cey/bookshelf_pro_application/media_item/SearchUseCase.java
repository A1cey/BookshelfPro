package org.a1cey.bookshelf_pro_application.media_item;

import java.util.stream.Collectors;

import org.a1cey.bookshelf_pro_application.dto.BookDto;
import org.a1cey.bookshelf_pro_application.dto.MovieDto;
import org.a1cey.bookshelf_pro_application.media_item.command.SearchCommand;
import org.a1cey.bookshelf_pro_application.media_item.result.SearchResult;
import org.a1cey.bookshelf_pro_domain.media_item.MediaItemRepository;
import org.a1cey.bookshelf_pro_domain.media_item.book.Book;
import org.a1cey.bookshelf_pro_domain.media_item.movie.Movie;
import org.springframework.stereotype.Service;

@Service
public class SearchUseCase {
    private final MediaItemRepository mediaItemRepository;

    public SearchUseCase(MediaItemRepository mediaItemRepository) {
        this.mediaItemRepository = mediaItemRepository;
    }

    public SearchResult execute(SearchCommand searchCommamd) {
        var found = mediaItemRepository.search(searchCommamd.mediaItemSearchCriteria())
                                       .stream()
                                       .map(mediaItem -> switch (mediaItem) {
                                           case Book book -> BookDto.from(book);
                                           case Movie movie -> MovieDto.from(movie);
                                           default -> throw new IllegalStateException("Unexpected media item type: " + mediaItem);
                                       })
                                       .collect(Collectors.toSet());

        return new SearchResult(found);
    }
}
