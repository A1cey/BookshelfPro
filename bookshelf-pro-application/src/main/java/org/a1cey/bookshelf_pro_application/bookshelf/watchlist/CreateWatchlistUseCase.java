package org.a1cey.bookshelf_pro_application.bookshelf.watchlist;

import org.a1cey.bookshelf_pro_application.IdService;
import org.a1cey.bookshelf_pro_application.bookshelf.watchlist.command.CreateWatchlistCommand;
import org.a1cey.bookshelf_pro_application.bookshelf.watchlist.result.CreateWatchlistResult;
import org.a1cey.bookshelf_pro_application.security.CurrentUserProvider;
import org.a1cey.bookshelf_pro_domain.bookshelf.watchlist.Watchlist;
import org.a1cey.bookshelf_pro_domain.bookshelf.watchlist.WatchlistId;
import org.a1cey.bookshelf_pro_domain.bookshelf.watchlist.WatchlistRepository;
import org.springframework.stereotype.Service;

@Service
public class CreateWatchlistUseCase {
    private final WatchlistRepository watchlistRepository;
    private final IdService idService;
    private final CurrentUserProvider currentUserProvider;

    public CreateWatchlistUseCase(
        WatchlistRepository watchlistRepository,
        IdService idService,
        CurrentUserProvider currentUserProvider) {
        this.watchlistRepository = watchlistRepository;
        this.idService = idService;
        this.currentUserProvider = currentUserProvider;
    }

    public CreateWatchlistResult execute(CreateWatchlistCommand command) {
        var owner = currentUserProvider.currentUser();
        var watchlistId = new WatchlistId(idService.generateId());

        var watchlist = new Watchlist(watchlistId, owner.id(), command.title(), command.itemsAsBookshelfEntryIds());

        watchlistRepository.save(watchlist);

        return new CreateWatchlistResult(watchlistId.value());
    }
}
