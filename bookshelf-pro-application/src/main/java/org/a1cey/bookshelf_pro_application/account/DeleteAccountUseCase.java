package org.a1cey.bookshelf_pro_application.account;

import org.a1cey.bookshelf_pro_application.security.CurrentUserProvider;
import org.a1cey.bookshelf_pro_domain.account.AccountRepository;
import org.springframework.stereotype.Service;

@Service
public final class DeleteAccountUseCase {
    private final AccountRepository accountRepository;
    private final CurrentUserProvider currentUserProvider;

    public DeleteAccountUseCase(AccountRepository accountRepository, CurrentUserProvider currentUserProvider) {
        this.accountRepository = accountRepository;
        this.currentUserProvider = currentUserProvider;
    }

    public void execute() {
        var currentUser = currentUserProvider.currentUser();

        var account = accountRepository.findById(currentUser.id())
                                       .orElseThrow(() -> new IllegalArgumentException("Account not found"));

        account.delete(account.id());
        accountRepository.delete(account.id());
    }

}
