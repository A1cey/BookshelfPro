package org.a1cey.bookshelf_pro_plugins.rest.bookshelf.playlist.request;

import java.util.List;
import java.util.UUID;

public record CreatePlaylistRequest(
    String title,
    List<UUID> itemsAsBookshelfEntryIds
) {}
