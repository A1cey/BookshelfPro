package org.a1cey.bookshelf_pro_application.account.command;

import java.util.Optional;

import org.a1cey.bookshelf_pro_domain.account.Email;
import org.a1cey.bookshelf_pro_domain.account.Username;

public record UpdateAccountCommand(
    Optional<Username> newName,
    Optional<String> newRawPassword,
    Optional<Email> newEmail
) {}
