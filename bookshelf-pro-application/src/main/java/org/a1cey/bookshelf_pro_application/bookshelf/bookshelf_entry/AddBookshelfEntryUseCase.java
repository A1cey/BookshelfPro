package org.a1cey.bookshelf_pro_application.bookshelf.bookshelf_entry;

import org.a1cey.bookshelf_pro_application.IdService;
import org.a1cey.bookshelf_pro_application.bookshelf.bookshelf_entry.command.AddBookshelfEntryCommand;
import org.a1cey.bookshelf_pro_application.bookshelf.bookshelf_entry.result.AddBookshelfEntryResult;
import org.a1cey.bookshelf_pro_application.security.CurrentUserProvider;
import org.a1cey.bookshelf_pro_domain.bookshelf.bookshelf_entry.BookshelfEntry;
import org.a1cey.bookshelf_pro_domain.bookshelf.bookshelf_entry.BookshelfEntryId;
import org.a1cey.bookshelf_pro_domain.bookshelf.bookshelf_entry.BookshelfEntryRepository;
import org.a1cey.bookshelf_pro_domain.bookshelf.bookshelf_entry.consumption.ConsumptionProgress;
import org.a1cey.bookshelf_pro_domain.bookshelf.bookshelf_entry.consumption.ConsumptionProgressId;
import org.a1cey.bookshelf_pro_domain.bookshelf.bookshelf_entry.consumption.ConsumptionState;
import org.a1cey.bookshelf_pro_domain.media_item.MediaItemRepository;
import org.a1cey.bookshelf_pro_domain.media_item.book.Book;
import org.a1cey.bookshelf_pro_domain.media_item.book.PageCount;
import org.a1cey.bookshelf_pro_domain.media_item.movie.Duration;
import org.a1cey.bookshelf_pro_domain.media_item.movie.Movie;
import org.springframework.stereotype.Service;

@Service
public class AddBookshelfEntryUseCase {

    private final BookshelfEntryRepository bookshelfEntryRepository;
    private final MediaItemRepository mediaItemRepository;
    private final IdService idService;
    private final CurrentUserProvider currentUserProvider;

    public AddBookshelfEntryUseCase(
        BookshelfEntryRepository bookshelfEntryRepository,
        MediaItemRepository mediaItemRepository,
        IdService idService,
        CurrentUserProvider currentUserProvider) {
        this.bookshelfEntryRepository = bookshelfEntryRepository;
        this.mediaItemRepository = mediaItemRepository;
        this.idService = idService;
        this.currentUserProvider = currentUserProvider;
    }

    public AddBookshelfEntryResult execute(AddBookshelfEntryCommand command) {
        var owner = currentUserProvider.currentUser();

        if (bookshelfEntryRepository.existsByAccountAndMediaItem(owner.id(), command.mediaItemId())) {
            throw new IllegalArgumentException("Bookshelf entry already exists");
        }

        var mediaItem = mediaItemRepository
                            .findById(command.mediaItemId())
                            .orElseThrow(() -> new IllegalArgumentException("Invalid media" + " item id."));

        var consumptionProgressId = new ConsumptionProgressId(idService.generateId());
        var mediaConsumptionProgress = switch (mediaItem) {
            case Book book -> book.createProgress(new PageCount(0));
            case Movie movie -> movie.createProgress(Duration.of(0));
            default -> throw new IllegalStateException("Unexpected media item type: " + mediaItem);
        };
        var consumptionProgress = new ConsumptionProgress(consumptionProgressId, mediaConsumptionProgress, ConsumptionState.NOT_STARTED);

        var bookshelfEntryId = new BookshelfEntryId(idService.generateId());
        var bookshelfEntry = BookshelfEntry.builder(
                bookshelfEntryId,
                mediaItem.id(),
                owner.id(),
                consumptionProgress
            ).labels(command.labels())
             .build();

        bookshelfEntryRepository.save(bookshelfEntry, mediaItem.type());

        return new AddBookshelfEntryResult(bookshelfEntryId.value());
    }
}
