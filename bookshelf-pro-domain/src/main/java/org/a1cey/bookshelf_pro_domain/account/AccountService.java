package org.a1cey.bookshelf_pro_domain.account;

import org.jmolecules.ddd.annotation.Service;
import org.jspecify.annotations.Nullable;

import jakarta.validation.Valid;

@Service
public final class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account createAccount(AccountId id, @Valid Username name, @Nullable @Valid Email email, @Valid Password password) {
        validateUserNameIsUnique(name);
        return new Account(id, name, email, password);
    }

    public void changeUsername(Account account, Username newUsername, AccountId userRequestingChange) {
        if (account.name() == newUsername) {
            return;
        }

        // Calling this after previous check ensures, that no exception is thrown here if the new username is the same as the old
        validateUserNameIsUnique(newUsername);

        account.changeName(newUsername, userRequestingChange);
    }

    private void validateUserNameIsUnique(Username name) {
        if (this.accountRepository.existsUsername(name)) {
            throw new IllegalStateException("Username " + name + " already exists");
        }
    }

}
