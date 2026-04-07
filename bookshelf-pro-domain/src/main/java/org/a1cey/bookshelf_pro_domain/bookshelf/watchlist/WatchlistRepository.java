package org.a1cey.bookshelf_pro_domain.bookshelf.watchlist;

import java.util.Optional;
import java.util.Set;

import org.a1cey.bookshelf_pro_domain.account.AccountId;
import org.jmolecules.ddd.annotation.Repository;

@Repository
public interface WatchlistRepository {
    void save(Watchlist watchlist);

    void update(Watchlist watchlist);

    void delete(Watchlist watchlist);

    Optional<Watchlist> findById(WatchlistId watchlistId);

    Optional<Watchlist> findByIdAndOwner(WatchlistId watchlistId, AccountId owner);

    Set<Watchlist> findByOwner(AccountId owner);
}
