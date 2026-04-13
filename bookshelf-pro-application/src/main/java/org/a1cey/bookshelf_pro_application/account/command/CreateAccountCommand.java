package org.a1cey.bookshelf_pro_application.account.command;

import java.util.Optional;

import org.a1cey.bookshelf_pro_domain.account.Email;
import org.a1cey.bookshelf_pro_domain.account.Username;

public record CreateAccountCommand(Username name, Optional<Email> email, String rawPassword) {}
