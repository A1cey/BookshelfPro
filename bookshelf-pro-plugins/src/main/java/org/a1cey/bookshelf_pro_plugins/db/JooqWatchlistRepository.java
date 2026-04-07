package org.a1cey.bookshelf_pro_plugins.db;

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.SequencedSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.a1cey.bookshelf_pro_domain.Title;
import org.a1cey.bookshelf_pro_domain.account.AccountId;
import org.a1cey.bookshelf_pro_domain.bookshelf.bookshelf_entry.BookshelfEntryId;
import org.a1cey.bookshelf_pro_domain.bookshelf.watchlist.Watchlist;
import org.a1cey.bookshelf_pro_domain.bookshelf.watchlist.WatchlistId;
import org.a1cey.bookshelf_pro_domain.bookshelf.watchlist.WatchlistRepository;
import org.a1cey.bookshelf_pro_plugins.db.jooq.tables.records.WatchlistItemsRecord;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import static org.a1cey.bookshelf_pro_plugins.db.jooq.Tables.WATCHLISTS;
import static org.a1cey.bookshelf_pro_plugins.db.jooq.Tables.WATCHLIST_ITEMS;

@Repository
public class JooqWatchlistRepository implements WatchlistRepository {
    private final DSLContext dsl;

    public JooqWatchlistRepository(DSLContext dsl) {
        this.dsl = dsl;
    }

    @Transactional
    @Override
    public void save(Watchlist watchlist) {
        dsl.insertInto(WATCHLISTS)
           .set(WATCHLISTS.ID, watchlist.id().value())
           .set(WATCHLISTS.OWNER, watchlist.owner().value())
           .set(WATCHLISTS.TITLE, watchlist.title().title())
           .execute();

        saveWatchlistItems(watchlist);
    }

    @Transactional
    @Override
    public void update(Watchlist watchlist) {
        dsl.update(WATCHLISTS)
           .set(WATCHLISTS.TITLE, watchlist.title().title())
           .where(WATCHLISTS.ID.eq(watchlist.id().value()))
           .execute();

        updateWatchlistItems(watchlist);
    }

    @Transactional
    @Override
    public void delete(Watchlist watchlist) {
        deleteWatchlistItems(watchlist);
        dsl.deleteFrom(WATCHLISTS)
           .where(WATCHLISTS.ID.eq(watchlist.id().value()))
           .execute();
    }

    @Override
    public Optional<Watchlist> findById(WatchlistId watchlistId) {
        var record = dsl.fetchOne(WATCHLISTS, WATCHLISTS.ID.eq(watchlistId.value()));

        if (record == null) {
            return Optional.empty();
        }
        var owner = new AccountId(record.getOwner());
        var title = new Title(record.getTitle());
        var items = fetchWatchlistItems(watchlistId);

        return Optional.of(new Watchlist(watchlistId, owner, title, items));
    }

    @Override
    public Optional<Watchlist> findByIdAndOwner(WatchlistId watchlistId, AccountId owner) {
        var record = dsl.fetchOne(
            WATCHLISTS, WATCHLISTS.ID.eq(watchlistId.value())
                                     .and(WATCHLISTS.OWNER.eq(owner.value()))
        );

        if (record == null) {
            return Optional.empty();
        }
        var title = new Title(record.getTitle());
        var items = fetchWatchlistItems(watchlistId);

        return Optional.of(new Watchlist(watchlistId, owner, title, items));
    }

    @Override
    public Set<Watchlist> findByOwner(AccountId owner) {
        return dsl.fetch(WATCHLISTS, WATCHLISTS.OWNER.eq(owner.value()))
                  .stream()
                  .map(record -> {
                      var watchlistId = new WatchlistId(record.getId());
                      var title = new Title(record.getTitle());
                      var items = fetchWatchlistItems(watchlistId);

                      return new Watchlist(watchlistId, owner, title, items);
                  })
                  .collect(Collectors.toSet());
    }

    private SequencedSet<BookshelfEntryId> fetchWatchlistItems(WatchlistId watchlistId) {
        var items = dsl.fetch(WATCHLIST_ITEMS, WATCHLIST_ITEMS.WATCHLIST_ID.eq(watchlistId.value()))
                       .stream()
                       .map(WatchlistItemsRecord::getBookshelfEntryId)
                       .map(BookshelfEntryId::new)
                       .toList();

        return new LinkedHashSet<>(items);
    }

    private void updateWatchlistItems(Watchlist watchlist) {
        deleteWatchlistItems(watchlist);
        saveWatchlistItems(watchlist);
    }

    private void deleteWatchlistItems(Watchlist watchlist) {
        dsl.deleteFrom(WATCHLIST_ITEMS)
           .where(WATCHLIST_ITEMS.WATCHLIST_ID.eq(watchlist.id().value()))
           .execute();
    }

    private void saveWatchlistItems(Watchlist watchlist) {
        dsl.insertInto(WATCHLIST_ITEMS, WATCHLIST_ITEMS.WATCHLIST_ID, WATCHLIST_ITEMS.BOOKSHELF_ENTRY_ID)
           .valuesOfRows(
               watchlist
                   .items()
                   .stream()
                   .map(bookshelfEntryId -> DSL.row(watchlist.id().value(), bookshelfEntryId.value()))
                   .toList()
           ).execute();
    }
}
