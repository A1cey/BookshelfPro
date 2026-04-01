package org.a1cey.bookshelf_pro_application;

import java.util.NoSuchElementException;

import org.a1cey.bookshelf_pro_domain.account.Account;
import org.a1cey.bookshelf_pro_domain.account.AccountId;
import org.a1cey.bookshelf_pro_domain.account.AccountRepository;
import org.a1cey.bookshelf_pro_domain.account.Password;
import org.a1cey.bookshelf_pro_domain.account.Username;
import org.springframework.stereotype.Service;

@Service
public final class SecurityService {
    AccountRepository accountRepository;

    public SecurityService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account checkUser(AccountId accountId, Username username, Password password) {
        var account = accountRepository
                          .findById(accountId)
                          .orElseThrow(() -> new NoSuchElementException("account not " + "found"));

        if (!account.name().equals(username)) {
            throw new IllegalArgumentException("account name does not belong to account id");
        }

        if (!account.password().equals(password)) {
            throw new SecurityException("Account does not belong to user");
        }

        return account;
    }
}
