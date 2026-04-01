package org.a1cey.bookshelf_pro_application.account;

import org.a1cey.bookshelf_pro_application.SecurityService;
import org.a1cey.bookshelf_pro_application.account.command.DeleteAccountCommand;
import org.a1cey.bookshelf_pro_domain.account.AccountRepository;
import org.springframework.stereotype.Service;

@Service
public final class DeleteAccountUseCase {
    private final AccountRepository accountRepository;
    private final SecurityService securityService;

    public DeleteAccountUseCase(AccountRepository accountRepository, SecurityService securityService) {
        this.accountRepository = accountRepository;
        this.securityService = securityService;
    }

    public void execute(DeleteAccountCommand command) {
        var account = securityService.checkUser(command.accountId(), command.name(), command.password());
        accountRepository.delete(account.id());
    }

}
