package org.a1cey.bookshelf_pro_plugins.db;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.a1cey.bookshelf_pro_domain.Title;
import org.a1cey.bookshelf_pro_domain.account.AccountId;
import org.a1cey.bookshelf_pro_domain.bookshelf.bookshelf_entry.BookshelfEntryId;
import org.a1cey.bookshelf_pro_domain.bookshelf.playlist.Playlist;
import org.a1cey.bookshelf_pro_domain.bookshelf.playlist.PlaylistId;
import org.a1cey.bookshelf_pro_domain.bookshelf.playlist.PlaylistPosition;
import org.a1cey.bookshelf_pro_domain.bookshelf.playlist.PlaylistRepository;
import org.a1cey.bookshelf_pro_plugins.db.jooq.tables.records.PlaylistItemsRecord;
import org.a1cey.bookshelf_pro_plugins.db.jooq.tables.records.PlaylistsRecord;
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

    @Transactional(readOnly = true)
    @Override
    public Optional<Playlist> findById(PlaylistId playlistId) {
        return dsl.fetchOptional(PLAYLISTS, PLAYLISTS.ID.eq(playlistId.value()))
                  .map(record -> new Playlist(
                      playlistId,
                      new AccountId(record.getOwner()),
                      new Title(record.getTitle()),
                      fetchPlaylistItems(playlistId)
                  ));
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Playlist> findByIdAndOwner(PlaylistId playlistId, AccountId owner) {
        return dsl.fetchOptional(PLAYLISTS, PLAYLISTS.ID.eq(playlistId.value()).and(PLAYLISTS.OWNER.eq(owner.value())))
                  .map(record -> new Playlist(
                      playlistId,
                      owner,
                      new Title(record.getTitle()),
                      fetchPlaylistItems(playlistId)
                  ));
    }

    @Transactional(readOnly = true)
    @Override
    public Set<Playlist> findByOwner(AccountId owner) {
        var records = dsl.fetch(PLAYLISTS, PLAYLISTS.OWNER.eq(owner.value()));
        var ids = records.stream().map(PlaylistsRecord::getId).collect(Collectors.toSet());

        var playlistItems = fetchPlaylistItemsBatch(ids);

        return records.stream().map(record -> {
            var items = playlistItems.get(record.getId());

            var sortedItems =
                items.stream().sorted(Comparator.comparing(PlaylistEntry::pos)).map(PlaylistEntry::bookshelfEntryId).toList();

            var playlistId = new PlaylistId(record.getId());
            var title = new Title(record.getTitle());

            return new Playlist(playlistId, owner, title, sortedItems);
        }).collect(Collectors.toSet());
    }

    private List<BookshelfEntryId> fetchPlaylistItems(PlaylistId playlistId) {
        return dsl.fetch(PLAYLIST_ITEMS, PLAYLIST_ITEMS.PLAYLIST_ID.eq(playlistId.value()))
                  .stream()
                  .sorted(Comparator.comparing(PlaylistItemsRecord::getPosition)) // Just for safety, db insert order should be enough
                  .map(PlaylistItemsRecord::getBookshelfEntryId)
                  .map(BookshelfEntryId::new)
                  .toList();
    }

    private Map<UUID, List<PlaylistEntry>> fetchPlaylistItemsBatch(Set<UUID> playlistIds) {
        return dsl.fetch(PLAYLIST_ITEMS, PLAYLIST_ITEMS.PLAYLIST_ID.in(playlistIds))
                  .stream()
                  .collect(Collectors.groupingBy(
                      PlaylistItemsRecord::getPlaylistId,
                      Collectors.mapping(PlaylistEntry::from, Collectors.toList())
                  ));
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

    private record PlaylistEntry(BookshelfEntryId bookshelfEntryId, PlaylistPosition pos) {

        public static PlaylistEntry from(PlaylistItemsRecord record) {
            return new PlaylistEntry(new BookshelfEntryId(record.getBookshelfEntryId()), new PlaylistPosition(record.getPosition()));
        }
    }
}
