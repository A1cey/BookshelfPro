package org.a1cey.bookshelf_pro_application.account;

import org.a1cey.bookshelf_pro_application.SecurityService;
import org.a1cey.bookshelf_pro_application.account.command.GetAccountCommand;
import org.a1cey.bookshelf_pro_application.account.result.GetAccountResult;
import org.springframework.stereotype.Service;

@Service
public final class GetAccountUseCase {
    private final SecurityService securityService;

    public GetAccountUseCase(SecurityService securityService) {
        this.securityService = securityService;
    }

    public GetAccountResult execute(GetAccountCommand command) {
        var account = securityService.checkUser(command.accountId(), command.name(), command.password());
        return new GetAccountResult(account.id(), account.name(), account.email());
    }
}
