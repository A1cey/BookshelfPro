package org.a1cey.bookshelf_pro_plugins.security;

import java.util.Objects;

import org.a1cey.bookshelf_pro_application.security.CurrentUserProvider;
import org.a1cey.bookshelf_pro_domain.account.Account;
import org.a1cey.bookshelf_pro_domain.account.AccountId;
import org.a1cey.bookshelf_pro_domain.account.Password;
import org.a1cey.bookshelf_pro_domain.account.Username;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SpringSecurityCurrentUserProvider implements CurrentUserProvider {
    @Override
    public Account currentUser() {
        var principal = (AccountUserDetails) Objects.requireNonNull(
                                                        SecurityContextHolder
                                                            .getContext()
                                                            .getAuthentication())
                                                    .getPrincipal();
        assert principal != null;
        var accountId = new AccountId(principal.accountId());
        var username = new Username(principal.getUsername());
        var password = new Password(principal.getPassword());
        return new Account(accountId, username, null, password);
    }
}