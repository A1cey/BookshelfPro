package org.a1cey.bookshelf_pro_application.bookshelf.bookshelf_entry.command;

import org.a1cey.bookshelf_pro_domain.account.AccountId;
import org.a1cey.bookshelf_pro_domain.account.Password;
import org.a1cey.bookshelf_pro_domain.account.Username;

public record GetAllBookshelfEntriesCommand(
    AccountId accountId,
    Username name,
    Password password
) {}
