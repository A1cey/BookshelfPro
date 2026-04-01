package org.a1cey.bookshelf_pro_application.bookshelf_entry.command;

import java.util.Set;

import org.a1cey.bookshelf_pro_domain.account.AccountId;
import org.a1cey.bookshelf_pro_domain.account.Password;
import org.a1cey.bookshelf_pro_domain.account.Username;
import org.a1cey.bookshelf_pro_domain.bookshelf_entry.Label;
import org.a1cey.bookshelf_pro_domain.media_item.MediaItemId;

public record AddBookshelfEntryCommand(
    AccountId accountId,
    Username name,
    Password password,
    MediaItemId mediaItemId,
    Set<Label> labels
) {}
