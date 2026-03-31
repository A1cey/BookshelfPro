package org.a1cey.bookshelf_pro_application.account.result;

import java.util.Optional;

import org.a1cey.bookshelf_pro_domain.account.AccountId;
import org.a1cey.bookshelf_pro_domain.account.Email;
import org.a1cey.bookshelf_pro_domain.account.Username;

public record GetAccountResult(AccountId id, Username name, Optional<Email> email) {}

