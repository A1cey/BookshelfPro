package org.a1cey.bookshelf_pro_application.account;

import java.util.NoSuchElementException;

import org.a1cey.bookshelf_pro_application.account.command.DeleteAccountCommand;
import org.a1cey.bookshelf_pro_domain.account.AccountRepository;
import org.a1cey.bookshelf_pro_domain.account.AccountService;
import org.springframework.stereotype.Service;

@Service
public final class DeleteAccountUseCase {
    private final AccountRepository accountRepository;
    private final AccountService accountService;

    public DeleteAccountUseCase(AccountRepository accountRepository, AccountService accountService) { // TODO: this is a domain service
        // with jmolecules @Service
        this.accountRepository = accountRepository;
        this.accountService = accountService;
    }

    public void execute(DeleteAccountCommand command) {
        // TODO: Better security
        var account = accountRepository
                          .findById(command.accountId())
                          .orElseThrow(() -> new NoSuchElementException("account not " + "found"));

        if (!account.name().equals(command.name())) {
            throw new IllegalArgumentException("account name does not belong to account id");
        }

        if (!account.password().equals(command.password())) {
            throw new SecurityException("Account does not belong to user");
        }

        accountRepository.delete(account.id());
    }

}
