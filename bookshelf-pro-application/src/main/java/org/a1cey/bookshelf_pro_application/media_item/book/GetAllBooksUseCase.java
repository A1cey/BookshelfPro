package org.a1cey.bookshelf_pro_application.media_item.book;

import java.util.stream.Collectors;

import org.a1cey.bookshelf_pro_application.dto.BookDto;
import org.a1cey.bookshelf_pro_application.media_item.book.result.GetAllBooksResult;
import org.a1cey.bookshelf_pro_domain.media_item.MediaItemRepository;
import org.a1cey.bookshelf_pro_domain.media_item.MediaItemType;
import org.a1cey.bookshelf_pro_domain.media_item.book.Book;
import org.springframework.stereotype.Service;

@Service
public final class GetAllBooksUseCase {
    private final MediaItemRepository mediaItemRepository;

    public GetAllBooksUseCase(MediaItemRepository mediaItemRepository) {
        this.mediaItemRepository = mediaItemRepository;
    }

    public GetAllBooksResult execute() {
        var books = mediaItemRepository
                        .findByType(MediaItemType.BOOK)
                        .stream()
                        .map(mediaItem -> BookDto.from((Book) mediaItem))
                        .collect(Collectors.toSet());

        return new GetAllBooksResult(books);
    }
}
