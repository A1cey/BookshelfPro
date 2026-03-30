package org.a1cey.bookshelf_pro_application.account;

import org.a1cey.bookshelf_pro_application.IdService;
import org.a1cey.bookshelf_pro_application.account.command.CreateAccountCommand;
import org.a1cey.bookshelf_pro_application.account.result.CreateAccountResult;
import org.a1cey.bookshelf_pro_domain.account.AccountId;
import org.a1cey.bookshelf_pro_domain.account.AccountRepository;
import org.a1cey.bookshelf_pro_domain.account.AccountService;
import org.springframework.stereotype.Service;

@Service
public class CreateAccountUseCase {
    private final AccountRepository accountRepository;

    public CreateAccountUseCase(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public CreateAccountResult execute(CreateAccountCommand command) {
        AccountService accountService = new AccountService(accountRepository);
        var account = accountService.buildAccount(
            new AccountId(IdService.UUIDv4()),
            command.name(),
            command.email().orElse(null),
            command.password()
        );

        accountRepository.save(account);

        return new CreateAccountResult(account.id());
    }

}
