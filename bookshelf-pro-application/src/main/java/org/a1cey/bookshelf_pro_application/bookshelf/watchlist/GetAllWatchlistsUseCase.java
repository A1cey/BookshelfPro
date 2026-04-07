package org.a1cey.bookshelf_pro_application.bookshelf.watchlist;

import java.util.stream.Collectors;

import org.a1cey.bookshelf_pro_application.SecurityService;
import org.a1cey.bookshelf_pro_application.bookshelf.watchlist.command.GetAllWatchlistsCommand;
import org.a1cey.bookshelf_pro_application.bookshelf.watchlist.result.GetAllWatchlistResult;
import org.a1cey.bookshelf_pro_application.dto.WatchlistDto;
import org.a1cey.bookshelf_pro_domain.bookshelf.watchlist.WatchlistRepository;
import org.springframework.stereotype.Service;

@Service
public class GetAllWatchlistsUseCase {
    private final SecurityService securityService;
    private final WatchlistRepository watchlistRepository;

    public GetAllWatchlistsUseCase(
        SecurityService securityService,
        WatchlistRepository watchlistRepository
    ) {
        this.securityService = securityService;
        this.watchlistRepository = watchlistRepository;
    }

    public GetAllWatchlistResult execute(GetAllWatchlistsCommand command) {
        var account = securityService.checkUser(command.owner(), command.name(), command.password());

        var watchlists = watchlistRepository
                             .findByOwner(account.id())
                             .stream()
                             .map(WatchlistDto::from)
                             .collect(Collectors.toSet());

        return new GetAllWatchlistResult(watchlists);
    }
}
