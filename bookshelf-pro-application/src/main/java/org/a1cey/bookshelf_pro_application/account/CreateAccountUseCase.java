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
    private final IdService idService;

    public CreateAccountUseCase(AccountRepository accountRepository, AccountService accountService, IdService idService) {
        this.accountRepository = accountRepository;
        // TODO: this is a domain service with jmolecules @Service
        this.accountService = accountService;
        this.idService = idService;
    }

    public CreateAccountResult execute(CreateAccountCommand command) {
        var account = accountService.createAccount(
            new AccountId(idService.generateId()),
            command.name(),
            command.email().orElse(null),
            command.password()
        );

        accountRepository.save(account);

        return new CreateAccountResult(account.id().value());
    }

}
