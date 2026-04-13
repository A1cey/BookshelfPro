package org.a1cey.bookshelf_pro_plugins.security;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.jspecify.annotations.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public final class AccountUserDetails implements UserDetails {
    private final UUID accountId;
    private final String username;
    private final String hashedPassword;

    public AccountUserDetails(UUID accountId, String username, String hashedPassword) {
        this.accountId = accountId;
        this.username = username;
        this.hashedPassword = hashedPassword;
    }

    public UUID accountId() {
        return accountId;
    }

    @Override
    public @NonNull Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(); // no roles
    }

    @Override
    public String getPassword() {
        return hashedPassword;
    }

    @Override
    public @NonNull String getUsername() {
        return username;
    }
}
