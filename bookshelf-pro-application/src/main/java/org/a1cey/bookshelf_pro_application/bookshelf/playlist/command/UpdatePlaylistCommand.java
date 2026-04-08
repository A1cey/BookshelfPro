package org.a1cey.bookshelf_pro_application.bookshelf.playlist.command;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.a1cey.bookshelf_pro_domain.Title;
import org.a1cey.bookshelf_pro_domain.account.AccountId;
import org.a1cey.bookshelf_pro_domain.account.Password;
import org.a1cey.bookshelf_pro_domain.account.Username;
import org.a1cey.bookshelf_pro_domain.bookshelf.playlist.MovePlayListItem;
import org.a1cey.bookshelf_pro_domain.bookshelf.playlist.NewPlaylistItem;
import org.a1cey.bookshelf_pro_domain.bookshelf.playlist.PlaylistId;
import org.a1cey.bookshelf_pro_domain.bookshelf.playlist.PlaylistPosition;

public record UpdatePlaylistCommand(
    AccountId owner,
    Username name,
    Password password,
    PlaylistId playlistId,
    Optional<Title> newTitle,
    Optional<List<NewPlaylistItem>> newItems,
    Optional<Set<PlaylistPosition>> removeItemsFromPositions,
    Optional<Set<MovePlayListItem>> moveItems
) {}
