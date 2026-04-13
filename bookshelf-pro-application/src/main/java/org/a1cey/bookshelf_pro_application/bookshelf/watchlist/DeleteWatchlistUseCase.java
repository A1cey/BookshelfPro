package org.a1cey.bookshelf_pro_application.bookshelf.watchlist;

import org.a1cey.bookshelf_pro_application.bookshelf.watchlist.command.DeleteWatchlistCommand;
import org.a1cey.bookshelf_pro_application.security.CurrentUserProvider;
import org.a1cey.bookshelf_pro_domain.bookshelf.watchlist.WatchlistRepository;
import org.springframework.stereotype.Service;

@Service
public class DeleteWatchlistUseCase {
    private final WatchlistRepository watchlistRepository;
    private final CurrentUserProvider currentUserProvider;

    public DeleteWatchlistUseCase(WatchlistRepository watchlistRepository, CurrentUserProvider currentUserProvider) {
        this.watchlistRepository = watchlistRepository;
        this.currentUserProvider = currentUserProvider;
    }

    public void execute(DeleteWatchlistCommand command) {
        var owner = currentUserProvider.currentUser();
        var watchlist = watchlistRepository
                            .findById(command.watchlistId())
                            .orElseThrow(
                                () -> new IllegalArgumentException("Watchlist with id " + command.watchlistId().value() + " not found")
                            );

        if (!watchlist.owner().equals(owner.id())) {
            throw new SecurityException("User is not the owner of this watchlist");
        }

        watchlistRepository.delete(watchlist);
    }
}
