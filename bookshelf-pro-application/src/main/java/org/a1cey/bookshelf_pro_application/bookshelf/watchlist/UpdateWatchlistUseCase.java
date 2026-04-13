package org.a1cey.bookshelf_pro_application.bookshelf.watchlist;

import org.a1cey.bookshelf_pro_application.bookshelf.watchlist.command.UpdateWatchlistCommand;
import org.a1cey.bookshelf_pro_application.security.CurrentUserProvider;
import org.a1cey.bookshelf_pro_domain.bookshelf.bookshelf_entry.BookshelfEntryRepository;
import org.a1cey.bookshelf_pro_domain.bookshelf.watchlist.WatchlistRepository;
import org.springframework.stereotype.Service;

@Service
public class UpdateWatchlistUseCase {
    private final WatchlistRepository watchlistRepository;
    private final BookshelfEntryRepository bookshelfEntryRepository;
    private final CurrentUserProvider currentUserProvider;

    public UpdateWatchlistUseCase(
        WatchlistRepository watchlistRepository,
        BookshelfEntryRepository bookshelfEntryRepository,
        CurrentUserProvider currentUserProvider) {
        this.watchlistRepository = watchlistRepository;
        this.bookshelfEntryRepository = bookshelfEntryRepository;
        this.currentUserProvider = currentUserProvider;
    }

    public void execute(UpdateWatchlistCommand command) {
        var owner = currentUserProvider.currentUser();
        var watchlist = watchlistRepository
                            .findById(command.watchlistId())
                            .orElseThrow(
                                () -> new IllegalArgumentException("Watchlist with id " + command.watchlistId().value() + " not found")
                            );

        if (command.newTitle().isPresent()) {
            watchlist.changeTitle(command.newTitle().get(), owner.id());
        }

        if (command.newItemsAsBookshelfEntryIds().isPresent()) {
            command.newItemsAsBookshelfEntryIds().get().forEach(bookshelfEntryId -> {
                if (!bookshelfEntryRepository.existsByAccountAndId(owner.id(), bookshelfEntryId)) {
                    throw new IllegalArgumentException("Bookshelf entry with id " + bookshelfEntryId + " not found for user");
                }
                watchlist.addItem(bookshelfEntryId, owner.id());
            });
        }

        if (command.removeItemsAsBookshelfEntryIds().isPresent()) {
            command.removeItemsAsBookshelfEntryIds().get().forEach(bookshelfEntryId -> {
                if (!watchlist.items().contains(bookshelfEntryId)) {
                    throw new IllegalArgumentException(
                        "Bookshelf entry with id " + bookshelfEntryId + " not found in watchlist but is supposed to be removed"
                    );
                }
                watchlist.removeItem(bookshelfEntryId, owner.id());
            });
        }

        watchlistRepository.update(watchlist);
    }
}
