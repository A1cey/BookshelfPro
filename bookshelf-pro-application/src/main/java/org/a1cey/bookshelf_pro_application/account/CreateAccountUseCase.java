package org.a1cey.bookshelf_pro_application.account;

import org.a1cey.bookshelf_pro_application.IdService;
import org.a1cey.bookshelf_pro_application.account.command.CreateAccountCommand;
import org.a1cey.bookshelf_pro_application.account.result.CreateAccountResult;
import org.a1cey.bookshelf_pro_application.security.PasswordHasher;
import org.a1cey.bookshelf_pro_domain.account.AccountId;
import org.a1cey.bookshelf_pro_domain.account.AccountRepository;
import org.a1cey.bookshelf_pro_domain.account.AccountService;
import org.a1cey.bookshelf_pro_domain.account.Password;
import org.springframework.stereotype.Service;

@Service
public final class CreateAccountUseCase {
    private final AccountRepository accountRepository;
    private final AccountService accountService;
    private final IdService idService;
    private final PasswordHasher passwordHasher;

    public CreateAccountUseCase(
        AccountRepository accountRepository,
        AccountService accountService,
        IdService idService,
        PasswordHasher passwordHasher
    ) {
        this.accountRepository = accountRepository;
        this.accountService = accountService;
        this.idService = idService;
        this.passwordHasher = passwordHasher;
    }

    public CreateAccountResult execute(CreateAccountCommand command) {
        var hashedPassword = new Password(passwordHasher.hash(command.rawPassword()));

        var account = accountService.createAccount(
            new AccountId(idService.generateId()),
            command.name(),
            command.email().orElse(null),
            hashedPassword
        );

        accountRepository.save(account);
        return new CreateAccountResult(account.id().value());
    }

}
