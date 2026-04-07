package org.a1cey.bookshelf_pro_application.bookshelf.watchlist;

import org.a1cey.bookshelf_pro_application.IdService;
import org.a1cey.bookshelf_pro_application.SecurityService;
import org.a1cey.bookshelf_pro_application.bookshelf.watchlist.command.CreateWatchlistCommand;
import org.a1cey.bookshelf_pro_domain.bookshelf.watchlist.Watchlist;
import org.a1cey.bookshelf_pro_domain.bookshelf.watchlist.WatchlistId;
import org.a1cey.bookshelf_pro_domain.bookshelf.watchlist.WatchlistRepository;
import org.springframework.stereotype.Service;

@Service
public class CreateWatchlistUseCase {
    private final SecurityService securityService;
    private final WatchlistRepository watchlistRepository;
    private final IdService idService;

    public CreateWatchlistUseCase(
        SecurityService securityService,
        WatchlistRepository watchlistRepository,
        IdService idService
    ) {
        this.securityService = securityService;
        this.watchlistRepository = watchlistRepository;
        this.idService = idService;
    }

    public WatchlistId execute(CreateWatchlistCommand command) {
        var account = securityService.checkUser(command.owner(), command.name(), command.password());

        var watchlistId = new WatchlistId(idService.generateId());

        var watchlist = new Watchlist(watchlistId, account.id(), command.title(), command.itemsAsBookshelfEntryIds());

        watchlistRepository.save(watchlist);

        return watchlistId;
    }
}
