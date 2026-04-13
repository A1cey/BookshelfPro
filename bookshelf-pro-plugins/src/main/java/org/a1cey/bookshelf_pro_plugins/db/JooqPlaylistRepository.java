package org.a1cey.bookshelf_pro_plugins.db;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.a1cey.bookshelf_pro_domain.Title;
import org.a1cey.bookshelf_pro_domain.account.AccountId;
import org.a1cey.bookshelf_pro_domain.bookshelf.bookshelf_entry.BookshelfEntryId;
import org.a1cey.bookshelf_pro_domain.bookshelf.playlist.Playlist;
import org.a1cey.bookshelf_pro_domain.bookshelf.playlist.PlaylistId;
import org.a1cey.bookshelf_pro_domain.bookshelf.playlist.PlaylistRepository;
import org.a1cey.bookshelf_pro_plugins.db.jooq.tables.records.PlaylistItemsRecord;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import static org.a1cey.bookshelf_pro_plugins.db.jooq.Tables.PLAYLISTS;
import static org.a1cey.bookshelf_pro_plugins.db.jooq.Tables.PLAYLIST_ITEMS;

@Repository
public class JooqPlaylistRepository implements PlaylistRepository {
    private final DSLContext dsl;

    public JooqPlaylistRepository(DSLContext dsl) {
        this.dsl = dsl;
    }

    @Transactional
    @Override
    public void save(Playlist playlist) {
        dsl.insertInto(PLAYLISTS)
           .set(PLAYLISTS.ID, playlist.id().value())
           .set(PLAYLISTS.OWNER, playlist.owner().value())
           .set(PLAYLISTS.TITLE, playlist.title().title())
           .execute();

        savePlaylistItems(playlist);
    }

    @Transactional
    @Override
    public void update(Playlist playlist) {
        dsl.update(PLAYLISTS)
           .set(PLAYLISTS.TITLE, playlist.title().title())
           .where(PLAYLISTS.ID.eq(playlist.id().value()))
           .execute();

        updatePlaylistItems(playlist);
    }

    @Transactional
    @Override
    public void delete(Playlist playlist) {
        deletePlaylistItems(playlist);
        dsl.deleteFrom(PLAYLISTS)
           .where(PLAYLISTS.ID.eq(playlist.id().value()))
           .execute();
    }

    @Override
    public Optional<Playlist> findById(PlaylistId playlistId) {
        var record = dsl.fetchOne(PLAYLISTS, PLAYLISTS.ID.eq(playlistId.value()));

        if (record == null) {
            return Optional.empty();
        }
        var owner = new AccountId(record.getOwner());
        var title = new Title(record.getTitle());
        var items = fetchPlaylistItems(playlistId);

        return Optional.of(new Playlist(playlistId, owner, title, items));
    }

    @Override
    public Optional<Playlist> findByIdAndOwner(PlaylistId playlistId, AccountId owner) {
        var record = dsl.fetchOne(
            PLAYLISTS, PLAYLISTS.ID.eq(playlistId.value())
                                   .and(PLAYLISTS.OWNER.eq(owner.value()))
        );

        if (record == null) {
            return Optional.empty();
        }
        var title = new Title(record.getTitle());
        var items = fetchPlaylistItems(playlistId);

        return Optional.of(new Playlist(playlistId, owner, title, items));
    }

    @Override
    public Set<Playlist> findByOwner(AccountId owner) {
        return dsl.fetch(PLAYLISTS, PLAYLISTS.OWNER.eq(owner.value()))
                  .stream()
                  .map(record -> {
                      var playlistId = new PlaylistId(record.getId());
                      var title = new Title(record.getTitle());
                      var items = fetchPlaylistItems(playlistId);

                      return new Playlist(playlistId, owner, title, items);
                  })
                  .collect(Collectors.toSet());
    }

    private List<BookshelfEntryId> fetchPlaylistItems(PlaylistId playlistId) {
        return dsl.fetch(PLAYLIST_ITEMS, PLAYLIST_ITEMS.PLAYLIST_ID.eq(playlistId.value()))
                  .stream()
                  .sorted(Comparator.comparing(PlaylistItemsRecord::getPosition)) // Just for safety, db insert order should be enough
                  .map(PlaylistItemsRecord::getBookshelfEntryId)
                  .map(BookshelfEntryId::new)
                  .toList();
    }

    private void updatePlaylistItems(Playlist playlist) {
        deletePlaylistItems(playlist);
        savePlaylistItems(playlist);
    }

    private void deletePlaylistItems(Playlist playlist) {
        dsl.deleteFrom(PLAYLIST_ITEMS)
           .where(PLAYLIST_ITEMS.PLAYLIST_ID.eq(playlist.id().value()))
           .execute();
    }

    private void savePlaylistItems(Playlist playlist) {
        if (playlist.items().isEmpty()) {
            return;
        }

        dsl.insertInto(PLAYLIST_ITEMS, PLAYLIST_ITEMS.PLAYLIST_ID, PLAYLIST_ITEMS.BOOKSHELF_ENTRY_ID, PLAYLIST_ITEMS.POSITION)
           .valuesOfRows(
               IntStream
                   .range(0, playlist.items().size())
                   .mapToObj(idx -> DSL.row(playlist.id().value(), playlist.items().get(idx).value(), idx))
                   .toList()
           ).execute();
    }
}
