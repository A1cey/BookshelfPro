package org.a1cey.bookshelf_pro_application.account;

import java.util.NoSuchElementException;

import org.a1cey.bookshelf_pro_application.account.command.DeleteAccountCommand;
import org.a1cey.bookshelf_pro_domain.account.AccountRepository;
import org.springframework.stereotype.Service;

@Service
public final class DeleteAccountUseCase {
    private final AccountRepository accountRepository;

    public DeleteAccountUseCase(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
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
