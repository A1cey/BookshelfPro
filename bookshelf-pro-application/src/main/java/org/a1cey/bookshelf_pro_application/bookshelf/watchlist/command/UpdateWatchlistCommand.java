package org.a1cey.bookshelf_pro_application.bookshelf.watchlist.command;

import java.util.Optional;
import java.util.SequencedSet;
import java.util.Set;

import org.a1cey.bookshelf_pro_domain.Title;
import org.a1cey.bookshelf_pro_domain.account.AccountId;
import org.a1cey.bookshelf_pro_domain.account.Password;
import org.a1cey.bookshelf_pro_domain.account.Username;
import org.a1cey.bookshelf_pro_domain.bookshelf.bookshelf_entry.BookshelfEntryId;
import org.a1cey.bookshelf_pro_domain.bookshelf.watchlist.WatchlistId;

public record UpdateWatchlistCommand(
    AccountId owner,
    Username name,
    Password password,
    WatchlistId watchlistId,
    Optional<Title> newTitle,
    Optional<SequencedSet<BookshelfEntryId>> newItemsAsBookshelfEntryIds,
    Optional<Set<BookshelfEntryId>> removeItemsAsBookshelfEntryIds
) {}
