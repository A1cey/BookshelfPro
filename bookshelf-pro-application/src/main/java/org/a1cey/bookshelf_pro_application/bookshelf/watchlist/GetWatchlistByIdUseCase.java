package org.a1cey.bookshelf_pro_application.bookshelf.watchlist;

import java.util.Optional;

import org.a1cey.bookshelf_pro_application.SecurityService;
import org.a1cey.bookshelf_pro_application.bookshelf.watchlist.command.GetWatchlistByIdCommand;
import org.a1cey.bookshelf_pro_application.bookshelf.watchlist.result.GetWatchlistByIdResult;
import org.a1cey.bookshelf_pro_application.dto.WatchlistDto;
import org.a1cey.bookshelf_pro_domain.bookshelf.watchlist.WatchlistRepository;
import org.springframework.stereotype.Service;

@Service
public class GetWatchlistByIdUseCase {
    private final SecurityService securityService;
    private final WatchlistRepository watchlistRepository;

    public GetWatchlistByIdUseCase(
        SecurityService securityService,
        WatchlistRepository watchlistRepository
    ) {
        this.securityService = securityService;
        this.watchlistRepository = watchlistRepository;
    }

    public Optional<GetWatchlistByIdResult> execute(GetWatchlistByIdCommand command) {
        var account = securityService.checkUser(command.owner(), command.name(), command.password());

        return watchlistRepository
                   .findByIdAndOwner(command.watchlistId(), account.id())
                   .map(WatchlistDto::from)
                   .map(GetWatchlistByIdResult::new);
    }
}
