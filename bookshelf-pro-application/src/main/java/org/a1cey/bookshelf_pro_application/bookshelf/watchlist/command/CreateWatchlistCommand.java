package org.a1cey.bookshelf_pro_application.bookshelf.watchlist.command;

import java.util.SequencedSet;

import org.a1cey.bookshelf_pro_domain.Title;
import org.a1cey.bookshelf_pro_domain.account.AccountId;
import org.a1cey.bookshelf_pro_domain.account.Password;
import org.a1cey.bookshelf_pro_domain.account.Username;
import org.a1cey.bookshelf_pro_domain.bookshelf.bookshelf_entry.BookshelfEntryId;

public record CreateWatchlistCommand(
    AccountId owner,
    Username name,
    Password password,
    Title title,
    SequencedSet<BookshelfEntryId> itemsAsBookshelfEntryIds
) {}
