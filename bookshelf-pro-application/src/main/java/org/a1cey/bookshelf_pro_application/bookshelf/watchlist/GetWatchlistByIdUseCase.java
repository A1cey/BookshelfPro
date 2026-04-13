package org.a1cey.bookshelf_pro_application.bookshelf.watchlist;

import java.util.Optional;

import org.a1cey.bookshelf_pro_application.bookshelf.watchlist.command.GetWatchlistByIdCommand;
import org.a1cey.bookshelf_pro_application.bookshelf.watchlist.result.GetWatchlistByIdResult;
import org.a1cey.bookshelf_pro_application.dto.WatchlistDto;
import org.a1cey.bookshelf_pro_application.security.CurrentUserProvider;
import org.a1cey.bookshelf_pro_domain.bookshelf.watchlist.WatchlistRepository;
import org.springframework.stereotype.Service;

@Service
public class GetWatchlistByIdUseCase {
    private final WatchlistRepository watchlistRepository;
    private final CurrentUserProvider currentUserProvider;

    public GetWatchlistByIdUseCase(WatchlistRepository watchlistRepository, CurrentUserProvider currentUserProvider) {
        this.watchlistRepository = watchlistRepository;
        this.currentUserProvider = currentUserProvider;
    }

    public Optional<GetWatchlistByIdResult> execute(GetWatchlistByIdCommand command) {
        var owner = currentUserProvider.currentUser();
        return watchlistRepository
                   .findByIdAndOwner(command.watchlistId(), owner.id())
                   .map(WatchlistDto::from)
                   .map(GetWatchlistByIdResult::new);
    }
}
