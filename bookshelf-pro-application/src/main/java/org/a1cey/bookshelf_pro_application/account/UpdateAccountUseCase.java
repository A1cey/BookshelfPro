package org.a1cey.bookshelf_pro_application.account;

import org.a1cey.bookshelf_pro_application.SecurityService;
import org.a1cey.bookshelf_pro_application.account.command.UpdateAccountCommand;
import org.a1cey.bookshelf_pro_domain.account.AccountRepository;
import org.a1cey.bookshelf_pro_domain.account.AccountService;
import org.springframework.stereotype.Service;

@Service
public final class UpdateAccountUseCase {
    private final AccountRepository accountRepository;
    private final AccountService accountService;
    private final SecurityService securityService;

    public UpdateAccountUseCase(AccountRepository accountRepository, AccountService accountService, SecurityService securityService) { //
        this.accountRepository = accountRepository;
        // TODO: this is a domain service with jmolecules @Service
        this.accountService = accountService;
        this.securityService = securityService;
    }

    public void execute(UpdateAccountCommand command) {
        var account = securityService.checkUser(command.accountId(), command.name(), command.password());

        command.newName().ifPresent(newName -> accountService.changeUsername(account, newName, command.accountId()));
        command.newPassword().ifPresent(newPassword -> account.changePassword(newPassword, command.accountId()));
        command.newEmail().ifPresent(newEmail -> account.changeEmail(newEmail, command.accountId()));

        accountRepository.update(account);
    }
}
