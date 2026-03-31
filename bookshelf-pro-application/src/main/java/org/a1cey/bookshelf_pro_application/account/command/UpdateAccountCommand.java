package org.a1cey.bookshelf_pro_application.account.command;

import java.util.Optional;

import org.a1cey.bookshelf_pro_domain.account.AccountId;
import org.a1cey.bookshelf_pro_domain.account.Email;
import org.a1cey.bookshelf_pro_domain.account.Password;
import org.a1cey.bookshelf_pro_domain.account.Username;

public record UpdateAccountCommand(
    AccountId accountId,
    Username name,
    Password password,
    Optional<Username> newName,
    Optional<Password> newPassword,
    Optional<Email> newEmail
) {}
