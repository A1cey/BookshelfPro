package org.a1cey.bookshelf_pro_application.account;

import java.util.Optional;

import org.a1cey.bookshelf_pro_application.account.command.GetAccountCommand;
import org.a1cey.bookshelf_pro_application.account.result.GetAccountResult;
import org.a1cey.bookshelf_pro_domain.account.AccountRepository;
import org.springframework.stereotype.Service;

@Service
public final class GetAccountUseCase {
    private final AccountRepository accountRepository;

    public GetAccountUseCase(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Optional<GetAccountResult> execute(GetAccountCommand command) {
        return accountRepository
                   .findById(command.accountId())
                   .map(account -> {
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
