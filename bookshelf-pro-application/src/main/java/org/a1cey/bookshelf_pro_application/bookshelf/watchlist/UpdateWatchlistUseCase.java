package org.a1cey.bookshelf_pro_application.bookshelf.watchlist;

import org.a1cey.bookshelf_pro_application.SecurityService;
import org.a1cey.bookshelf_pro_application.bookshelf.watchlist.command.UpdateWatchlistCommand;
import org.a1cey.bookshelf_pro_domain.bookshelf.bookshelf_entry.BookshelfEntryRepository;
import org.a1cey.bookshelf_pro_domain.bookshelf.watchlist.WatchlistRepository;
import org.springframework.stereotype.Service;

@Service
public class UpdateWatchlistUseCase {
    private final SecurityService securityService;
    private final WatchlistRepository watchlistRepository;
    private final BookshelfEntryRepository bookshelfEntryRepository;

    public UpdateWatchlistUseCase(
        SecurityService securityService,
        WatchlistRepository watchlistRepository,
        BookshelfEntryRepository bookshelfEntryRepository
    ) {
        this.securityService = securityService;
        this.watchlistRepository = watchlistRepository;
        this.bookshelfEntryRepository = bookshelfEntryRepository;
    }

    public void execute(UpdateWatchlistCommand command) {
        var account = securityService.checkUser(command.owner(), command.name(), command.password());

        var watchlist = watchlistRepository
                            .findById(command.watchlistId())
                            .orElseThrow(
                                () -> new IllegalArgumentException("Watchlist with id " + command.watchlistId().value() + " not found")
                            );

        if (command.newTitle().isPresent()) {
            watchlist.changeTitle(command.newTitle().get(), account.id());
        }

        if (command.newItemsAsBookshelfEntryIds().isPresent()) {
            command.newItemsAsBookshelfEntryIds().get().forEach(bookshelfEntryId -> {
                if (!bookshelfEntryRepository.existsByAccountAndId(account.id(), bookshelfEntryId)) {
                    throw new IllegalArgumentException("Bookshelf entry with id " + bookshelfEntryId + " not found for user");
                }
                watchlist.addItem(bookshelfEntryId, account.id());
            });
        }

        if (command.removeItemsAsBookshelfEntryIds().isPresent()) {
            command.removeItemsAsBookshelfEntryIds().get().forEach(bookshelfEntryId -> {
                if (!watchlist.items().contains(bookshelfEntryId)) {
                    throw new IllegalArgumentException(
                        "Bookshelf entry with id " + bookshelfEntryId + " not found in watchlist but is supposed to be removed"
                    );
                }
                watchlist.removeItem(bookshelfEntryId, account.id());
            });
        }

        watchlistRepository.update(watchlist);
    }
}
