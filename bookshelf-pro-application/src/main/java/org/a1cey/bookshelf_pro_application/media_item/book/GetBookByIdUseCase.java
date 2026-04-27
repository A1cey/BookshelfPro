package org.a1cey.bookshelf_pro_application.media_item.book;

import java.util.Optional;

import org.a1cey.bookshelf_pro_application.dto.BookDto;
import org.a1cey.bookshelf_pro_application.media_item.book.command.GetBookCommand;
import org.a1cey.bookshelf_pro_application.media_item.book.result.GetBookByIdResult;
import org.a1cey.bookshelf_pro_domain.media_item.MediaItemRepository;
import org.a1cey.bookshelf_pro_domain.media_item.book.Book;
import org.springframework.stereotype.Service;

@Service
public final class GetBookByIdUseCase {
    private final MediaItemRepository mediaItemRepository;

    public GetBookByIdUseCase(MediaItemRepository mediaItemRepository) {
        this.mediaItemRepository = mediaItemRepository;
    }

    public Optional<GetBookByIdResult> execute(GetBookCommand command) {
        return mediaItemRepository.findById(command.bookId()).map(mediaItem -> {
            if (!(mediaItem instanceof Book book)) {
                throw new IllegalArgumentException("Id " + command.bookId() + " exists but is not a book but a " + mediaItem.type());
            }

            return book;
        }).map(b -> new GetBookByIdResult(BookDto.from(b)));
    }
}
