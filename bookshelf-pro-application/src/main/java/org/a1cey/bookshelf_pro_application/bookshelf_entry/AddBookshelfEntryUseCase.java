package org.a1cey.bookshelf_pro_application.bookshelf_entry;

import org.a1cey.bookshelf_pro_application.IdService;
import org.a1cey.bookshelf_pro_application.SecurityService;
import org.a1cey.bookshelf_pro_application.bookshelf_entry.command.AddBookshelfEntryCommand;
import org.a1cey.bookshelf_pro_domain.bookshelf_entry.BookshelfEntry;
import org.a1cey.bookshelf_pro_domain.bookshelf_entry.BookshelfEntryId;
import org.a1cey.bookshelf_pro_domain.bookshelf_entry.BookshelfEntryRepository;
import org.a1cey.bookshelf_pro_domain.bookshelf_entry.consumption.ConsumptionProgress;
import org.a1cey.bookshelf_pro_domain.bookshelf_entry.consumption.ConsumptionProgressId;
import org.a1cey.bookshelf_pro_domain.media_item.MediaItemRepository;
import org.a1cey.bookshelf_pro_domain.media_item.book.Book;
import org.a1cey.bookshelf_pro_domain.media_item.book.PageCount;
import org.springframework.stereotype.Service;

@Service
public class AddBookshelfEntryUseCase {

    private final SecurityService securityService;
    private final BookshelfEntryRepository bookshelfEntryRepository;
    private final MediaItemRepository mediaItemRepository;
    private final IdService idService;

    public AddBookshelfEntryUseCase(SecurityService securityService, BookshelfEntryRepository bookshelfEntryRepository,
                                    MediaItemRepository mediaItemRepository, IdService idService) {
        this.securityService = securityService;
        this.bookshelfEntryRepository = bookshelfEntryRepository;
        this.mediaItemRepository = mediaItemRepository;
        this.idService = idService;
    }

    public BookshelfEntry execute(AddBookshelfEntryCommand command) {
        var owner = securityService.checkUser(command.accountId(), command.name(), command.password());

        if (bookshelfEntryRepository.existsByAccountAndMediaItem(owner.id(), command.mediaItemId())) {
            throw new IllegalArgumentException("Bookshelf entry already exists");
        }

        var mediaItem = mediaItemRepository
                            .findById(command.mediaItemId())
                            .orElseThrow(() -> new IllegalArgumentException("Invalid media" + " item id."));

        var consumptionProgressId = new ConsumptionProgressId(idService.generateId());
        var mediaConsumptionProgress = switch (mediaItem) {
            case Book book -> book.createProgress(new PageCount(0));
            default -> throw new IllegalStateException("Unexpected media item type: " + mediaItem);
        };
        var consumptionProgress = new ConsumptionProgress(consumptionProgressId, mediaConsumptionProgress);

        var bookshelfEntryId = new BookshelfEntryId(idService.generateId());
        var bookshelfEntry = BookshelfEntry.builder(
                bookshelfEntryId,
                mediaItem.id(),
                owner.id(),
                consumptionProgress
            ).labels(command.labels())
             .build();

        bookshelfEntryRepository.save(bookshelfEntry, mediaItem.type());

        return bookshelfEntry;
    }
}
