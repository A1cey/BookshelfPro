package org.a1cey.bookshelf_pro_application.bookshelf.watchlist;

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

import org.a1cey.bookshelf_pro_application.bookshelf.watchlist.command.RemoveWatchlistItemsByConsumptionStateCommand;
import org.a1cey.bookshelf_pro_application.security.CurrentUserProvider;
import org.a1cey.bookshelf_pro_domain.bookshelf.bookshelf_entry.BookshelfEntry;
import org.a1cey.bookshelf_pro_domain.bookshelf.bookshelf_entry.BookshelfEntryRepository;
import org.a1cey.bookshelf_pro_domain.bookshelf.bookshelf_entry.consumption.ConsumptionState;
import org.a1cey.bookshelf_pro_domain.bookshelf.watchlist.WatchlistRepository;
import org.springframework.stereotype.Service;

@Service
public class RemoveWatchlistItemsByConsumptionStateUseCase {
    private final WatchlistRepository watchlistRepository;
    private final BookshelfEntryRepository bookshelfEntryRepository;
    private final CurrentUserProvider currentUserProvider;

    public RemoveWatchlistItemsByConsumptionStateUseCase(
        WatchlistRepository watchlistRepository,
        BookshelfEntryRepository bookshelfEntryRepository,
        CurrentUserProvider currentUserProvider) {
        this.watchlistRepository = watchlistRepository;
        this.bookshelfEntryRepository = bookshelfEntryRepository;
        this.currentUserProvider = currentUserProvider;
    }

    public void execute(RemoveWatchlistItemsByConsumptionStateCommand command) {
        var owner = currentUserProvider.currentUser();
        var watchlist = watchlistRepository
                            .findById(command.watchlistId())
                            .orElseThrow(
                                () -> new IllegalArgumentException("Watchlist with id " + command.watchlistId().value() + " not found")
                            );

        var remainingItems = watchlist
                                 .items()
                                 .stream()
                                 .map(bookshelfEntryRepository::findById)
                                 .flatMap(Optional::stream)
                                 .map(bookshelfEntry -> filterByConsumptionState(bookshelfEntry, command.consumptionStates()))
                                 .flatMap(Optional::stream)
                                 .map(BookshelfEntry::id)
                                 .toList();

        watchlist.changeItems(new LinkedHashSet<>(remainingItems), owner.id());
        watchlistRepository.update(watchlist);
    }

    private Optional<BookshelfEntry> filterByConsumptionState(BookshelfEntry bookshelfEntry, Set<ConsumptionState> consumptionStates) {
        if (consumptionStates.contains(ConsumptionState.COMPLETED)
                && bookshelfEntry.consumptionProgress().progress().isCompleted()) {
            return Optional.empty();
        }
        if (consumptionStates.contains(ConsumptionState.STARTED)
                && bookshelfEntry.consumptionProgress().progress().isStarted()) {
            return Optional.empty();
        }
        if (consumptionStates.contains(ConsumptionState.NOT_STARTED)
                && bookshelfEntry.consumptionProgress().progress().isNotStarted()) {
            return Optional.empty();
        }
        return Optional.of(bookshelfEntry);
    }
}
