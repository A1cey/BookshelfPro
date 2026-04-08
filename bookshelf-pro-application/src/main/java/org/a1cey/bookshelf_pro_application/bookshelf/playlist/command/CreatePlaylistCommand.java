package org.a1cey.bookshelf_pro_application.bookshelf.playlist.command;

import java.util.List;

import org.a1cey.bookshelf_pro_domain.Title;
import org.a1cey.bookshelf_pro_domain.account.AccountId;
import org.a1cey.bookshelf_pro_domain.account.Password;
import org.a1cey.bookshelf_pro_domain.account.Username;
import org.a1cey.bookshelf_pro_domain.bookshelf.bookshelf_entry.BookshelfEntryId;

public record CreatePlaylistCommand(
    AccountId owner,
    Username name,
    Password password,
    Title title,
    List<BookshelfEntryId> itemsAsBookshelfEntryIds
) {}
