package org.a1cey.bookshelf_pro_plugins.db;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Optional;
import java.util.SequencedSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.a1cey.bookshelf_pro_domain.Title;
import org.a1cey.bookshelf_pro_domain.account.AccountId;
import org.a1cey.bookshelf_pro_domain.bookshelf.bookshelf_entry.BookshelfEntryId;
import org.a1cey.bookshelf_pro_domain.bookshelf.watchlist.Watchlist;
import org.a1cey.bookshelf_pro_domain.bookshelf.watchlist.WatchlistId;
import org.a1cey.bookshelf_pro_domain.bookshelf.watchlist.WatchlistRepository;
import org.a1cey.bookshelf_pro_plugins.db.jooq.tables.records.PlaylistsRecord;
import org.a1cey.bookshelf_pro_plugins.db.jooq.tables.records.WatchlistItemsRecord;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import static org.a1cey.bookshelf_pro_plugins.db.jooq.Tables.PLAYLISTS;
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

    @Transactional(readOnly = true)
    @Override
    public Optional<Watchlist> findById(WatchlistId watchlistId) {
        return dsl.fetchOptional(WATCHLISTS, WATCHLISTS.ID.eq(watchlistId.value()))
                  .map(record -> new Watchlist(
                      watchlistId,
                      new AccountId(record.getOwner()),
                      new Title(record.getTitle()),
                      fetchWatchlistItems(watchlistId)
                  ));
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Watchlist> findByIdAndOwner(WatchlistId watchlistId, AccountId owner) {
        return dsl.fetchOptional(WATCHLISTS, WATCHLISTS.ID.eq(watchlistId.value()).and(WATCHLISTS.OWNER.eq(owner.value())))
                  .map(record -> new Watchlist(
                      watchlistId,
                      owner,
                      new Title(record.getTitle()),
                      fetchWatchlistItems(watchlistId)
                  ));
    }

    @Transactional(readOnly = true)
    @Override
    public Set<Watchlist> findByOwner(AccountId owner) {
        var records = dsl.fetch(PLAYLISTS, PLAYLISTS.OWNER.eq(owner.value()));
        var ids = records.stream().map(PlaylistsRecord::getId).collect(Collectors.toSet());

        var watchlistItems = fetchWatchlistItemsBatch(ids);

        return records.stream().map(record -> {
            var items = watchlistItems.get(record.getId());

            var watchlistId = new WatchlistId(record.getId());
            var title = new Title(record.getTitle());

            return new Watchlist(watchlistId, owner, title, items);
        }).collect(Collectors.toSet());
    }

    private SequencedSet<BookshelfEntryId> fetchWatchlistItems(WatchlistId watchlistId) {
        return dsl.fetch(WATCHLIST_ITEMS, WATCHLIST_ITEMS.WATCHLIST_ID.eq(watchlistId.value()))
                  .stream()
                  .map(WatchlistItemsRecord::getBookshelfEntryId)
                  .map(BookshelfEntryId::new)
                  .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    private Map<UUID, SequencedSet<BookshelfEntryId>> fetchWatchlistItemsBatch(Set<UUID> watchlistIds) {
        return dsl.fetch(WATCHLIST_ITEMS, WATCHLIST_ITEMS.WATCHLIST_ID.in(watchlistIds))
                  .stream()
                  .collect(Collectors.groupingBy(
                      WatchlistItemsRecord::getWatchlistId,
                      Collectors.mapping(
                          record -> new BookshelfEntryId(
                              record.getBookshelfEntryId()),
                          Collectors.toCollection(LinkedHashSet::new)
                      )
                  ));
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
        if (watchlist.items().isEmpty()) {
            return;
        }

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
