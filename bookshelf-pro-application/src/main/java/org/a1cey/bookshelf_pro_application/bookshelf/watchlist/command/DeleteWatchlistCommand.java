package org.a1cey.bookshelf_pro_application.bookshelf.watchlist.command;

import org.a1cey.bookshelf_pro_domain.account.AccountId;
import org.a1cey.bookshelf_pro_domain.account.Password;
import org.a1cey.bookshelf_pro_domain.account.Username;
import org.a1cey.bookshelf_pro_domain.bookshelf.watchlist.WatchlistId;

public record DeleteWatchlistCommand(
    AccountId owner,
    Username name,
    Password password,
    WatchlistId watchlistId
) {}
