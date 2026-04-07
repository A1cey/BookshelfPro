package org.a1cey.bookshelf_pro_application.bookshelf.watchlist.command;

import org.a1cey.bookshelf_pro_domain.account.AccountId;
import org.a1cey.bookshelf_pro_domain.account.Password;
import org.a1cey.bookshelf_pro_domain.account.Username;

public record GetAllWatchlistsCommand(
    AccountId owner,
    Username name,
    Password password
) {}
