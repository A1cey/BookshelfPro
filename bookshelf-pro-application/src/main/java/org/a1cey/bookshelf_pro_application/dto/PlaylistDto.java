package org.a1cey.bookshelf_pro_application.dto;

import java.util.List;
import java.util.UUID;

import org.a1cey.bookshelf_pro_domain.bookshelf.bookshelf_entry.BookshelfEntryId;
import org.a1cey.bookshelf_pro_domain.bookshelf.playlist.Playlist;

public record PlaylistDto(
    UUID playlistId,
    UUID owner,
    String title,
    List<UUID> itemsAsBookshelfEntryIds
) {

    public static PlaylistDto from(Playlist playlist) {
        return new PlaylistDto(
            playlist.id().value(),
            playlist.owner().value(),
            playlist.title().title(),
            playlist.items().stream().map(BookshelfEntryId::value).toList()
        );
    }
}
