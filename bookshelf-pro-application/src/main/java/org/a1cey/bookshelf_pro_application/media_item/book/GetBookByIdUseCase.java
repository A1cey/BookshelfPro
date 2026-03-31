package org.a1cey.bookshelf_pro_application.media_item.book;

import java.util.Optional;

import org.a1cey.bookshelf_pro_application.media_item.book.command.GetBookCommand;
import org.a1cey.bookshelf_pro_application.media_item.book.result.GetBookResult;
import org.a1cey.bookshelf_pro_domain.media_item.MediaItemRepository;
import org.a1cey.bookshelf_pro_domain.media_item.MediaItemType;
import org.a1cey.bookshelf_pro_domain.media_item.book.Book;
import org.springframework.stereotype.Service;

@Service
public final class GetBookUseCase {
    private final MediaItemRepository mediaItemRepository;

    public GetBookUseCase(MediaItemRepository mediaItemRepository) {
        this.mediaItemRepository = mediaItemRepository;
    }

    public Optional<GetBookResult> execute(GetBookCommand command) {
        var book = mediaItemRepository.findById(command.bookId()).map(mediaItem -> {
            if (mediaItem.type() != MediaItemType.BOOK) {
                throw new IllegalArgumentException("Id " + command.bookId() + " exists but is not a book but a " + mediaItem.type());
            }

            return (Book) mediaItem;
        });

        System.out.println(book);

        return book.map(b -> new GetBookResult(BookDto.from(b)));
    }
}
