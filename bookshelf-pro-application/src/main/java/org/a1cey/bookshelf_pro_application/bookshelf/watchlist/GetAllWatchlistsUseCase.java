package org.a1cey.bookshelf_pro_application.bookshelf.watchlist;

import java.util.stream.Collectors;

import org.a1cey.bookshelf_pro_application.bookshelf.watchlist.result.GetAllWatchlistsResult;
import org.a1cey.bookshelf_pro_application.dto.WatchlistDto;
import org.a1cey.bookshelf_pro_application.security.CurrentUserProvider;
import org.a1cey.bookshelf_pro_domain.bookshelf.watchlist.WatchlistRepository;
import org.springframework.stereotype.Service;

@Service
public class GetAllWatchlistsUseCase {
    private final WatchlistRepository watchlistRepository;
    private final CurrentUserProvider currentUserProvider;

    public GetAllWatchlistsUseCase(WatchlistRepository watchlistRepository, CurrentUserProvider currentUserProvider) {
        this.watchlistRepository = watchlistRepository;
        this.currentUserProvider = currentUserProvider;
    }

    public GetAllWatchlistsResult execute() {
        var owner = currentUserProvider.currentUser();

        var watchlists = watchlistRepository
                             .findByOwner(owner.id())
                             .stream()
                             .map(WatchlistDto::from)
                             .collect(Collectors.toSet());

        return new GetAllWatchlistsResult(watchlists);
    }
}
