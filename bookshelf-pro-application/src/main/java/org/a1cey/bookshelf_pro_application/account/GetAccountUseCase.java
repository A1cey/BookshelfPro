package org.a1cey.bookshelf_pro_application.account;

import java.util.Optional;

import org.a1cey.bookshelf_pro_application.account.command.GetAccountCommand;
import org.a1cey.bookshelf_pro_application.account.result.GetAccountResult;
import org.a1cey.bookshelf_pro_domain.account.AccountRepository;
import org.a1cey.bookshelf_pro_domain.account.AccountService;
import org.springframework.stereotype.Service;

@Service
public final class GetAccountUseCase {
    private final AccountRepository accountRepository;
    private final AccountService accountService;

    public GetAccountUseCase(AccountRepository accountRepository, AccountService accountService) { // TODO: this is a domain service
        // with jmolecules @Service
        this.accountRepository = accountRepository;
        this.accountService = accountService;
    }

    public Optional<GetAccountResult> execute(GetAccountCommand command) {
        return accountRepository
                   .findById(command.accountId()).map(account -> {
                if (!account.name().equals(command.name())) {
                    throw new IllegalArgumentException("account name does not belong to account id");
                }

                if (!account.password().equals(command.password())) {
                    throw new SecurityException("Account does not belong to user");
                }

                return new GetAccountResult(account.id(), account.name(), account.email());
            });
    }

}
