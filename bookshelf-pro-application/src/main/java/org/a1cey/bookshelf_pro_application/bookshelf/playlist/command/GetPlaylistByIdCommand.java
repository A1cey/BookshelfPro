package org.a1cey.bookshelf_pro_application.bookshelf.playlist.command;

import org.a1cey.bookshelf_pro_domain.account.AccountId;
import org.a1cey.bookshelf_pro_domain.account.Password;
import org.a1cey.bookshelf_pro_domain.account.Username;
import org.a1cey.bookshelf_pro_domain.bookshelf.playlist.PlaylistId;

public record GetPlaylistByIdCommand(
    AccountId owner,
    Username name,
    Password password,
    PlaylistId playlistId
) {}
