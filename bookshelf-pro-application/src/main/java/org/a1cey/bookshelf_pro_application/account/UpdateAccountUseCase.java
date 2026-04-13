package org.a1cey.bookshelf_pro_application.account;

import org.a1cey.bookshelf_pro_application.account.command.UpdateAccountCommand;
import org.a1cey.bookshelf_pro_application.security.CurrentUserProvider;
import org.a1cey.bookshelf_pro_application.security.PasswordHasher;
import org.a1cey.bookshelf_pro_domain.account.AccountRepository;
import org.a1cey.bookshelf_pro_domain.account.AccountService;
import org.a1cey.bookshelf_pro_domain.account.Password;
import org.springframework.stereotype.Service;

@Service
public final class UpdateAccountUseCase {
    private final AccountRepository accountRepository;
    private final AccountService accountService;
    private final CurrentUserProvider currentUserProvider;
    private final PasswordHasher passwordHasher;

    public UpdateAccountUseCase(
        AccountRepository accountRepository,
        AccountService accountService,
        CurrentUserProvider currentUserProvider,
        PasswordHasher passwordHasher) {
        this.accountRepository = accountRepository;
        this.accountService = accountService;
        this.currentUserProvider = currentUserProvider;
        this.passwordHasher = passwordHasher;
    }

    public void execute(UpdateAccountCommand command) {
        var account = currentUserProvider.currentUser();

        command.newName().ifPresent(newName -> accountService.changeUsername(account, newName, account.id()));
        command.newRawPassword().ifPresent(newRawPassword -> {
            var hashedPassword = new Password(passwordHasher.hash(newRawPassword));
            account.changePassword(hashedPassword, account.id());
        });
        command.newEmail().ifPresent(newEmail -> account.changeEmail(newEmail, account.id()));

        accountRepository.update(account);
    }
}
