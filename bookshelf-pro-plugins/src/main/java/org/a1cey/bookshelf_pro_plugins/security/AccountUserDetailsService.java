package org.a1cey.bookshelf_pro_plugins.security;

import org.a1cey.bookshelf_pro_domain.account.AccountRepository;
import org.a1cey.bookshelf_pro_domain.account.Username;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AccountUserDetailsService implements UserDetailsService {

    private final AccountRepository accountRepository;

    public AccountUserDetailsService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public @NonNull UserDetails loadUserByUsername(@NonNull String username) throws UsernameNotFoundException {
        return accountRepository
                   .findByUsername(new Username(username))
                   .map(account -> new AccountUserDetails(
                       account.id().value(),
                       account.name().name(),
                       account.password().hashedPassword()
                   ))
                   .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }
}