package org.a1cey.bookshelf_pro_application.bookshelf.watchlist;

import org.a1cey.bookshelf_pro_application.SecurityService;
import org.a1cey.bookshelf_pro_application.bookshelf.watchlist.command.DeleteWatchlistCommand;
import org.a1cey.bookshelf_pro_domain.bookshelf.watchlist.WatchlistRepository;
import org.springframework.stereotype.Service;

@Service
public class DeleteWatchlistUseCase {
    private final SecurityService securityService;
    private final WatchlistRepository watchlistRepository;

    public DeleteWatchlistUseCase(
        SecurityService securityService,
        WatchlistRepository watchlistRepository
    ) {
        this.securityService = securityService;
        this.watchlistRepository = watchlistRepository;
    }

    public void execute(DeleteWatchlistCommand command) {
        var account = securityService.checkUser(command.owner(), command.name(), command.password());

        var watchlist = watchlistRepository
                            .findById(command.watchlistId())
                            .orElseThrow(
                                () -> new IllegalArgumentException("Watchlist with id " + command.watchlistId().value() + " not found")
                            );

        if (!watchlist.owner().equals(account.id())) {
            throw new SecurityException("User is not the owner of this watchlist");
        }

        watchlistRepository.delete(watchlist);
    }
}
