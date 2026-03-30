package org.a1cey.bookshelf_pro_application.account;

import org.a1cey.bookshelf_pro_application.IdService;
import org.a1cey.bookshelf_pro_application.account.command.CreateAccountCommand;
import org.a1cey.bookshelf_pro_application.account.result.CreateAccountResult;
import org.a1cey.bookshelf_pro_domain.account.AccountId;
import org.a1cey.bookshelf_pro_domain.account.AccountRepository;
import org.a1cey.bookshelf_pro_domain.account.AccountService;
import org.springframework.stereotype.Service;

@Service
public final class CreateAccountUseCase {
    private final AccountRepository accountRepository;
    private final AccountService accountService;

    public CreateAccountUseCase(AccountRepository accountRepository, AccountService accountService) { // TODO: this is a domain service
        // with jmolecules @Service
        this.accountRepository = accountRepository;
        this.accountService = accountService;
    }

    public CreateAccountResult execute(CreateAccountCommand command) {
        var account = accountService.createAccount(
            new AccountId(IdService.UUIDv4()),
            command.name(),
            command.email().orElse(null),
            command.password()
        );

        accountRepository.save(account);

        return new CreateAccountResult(account.id());
    }

}
