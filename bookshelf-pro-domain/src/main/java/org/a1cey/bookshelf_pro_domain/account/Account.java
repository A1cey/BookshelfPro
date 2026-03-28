package org.a1cey.bookshelf_pro_domain.account;

import java.util.Optional;

import org.a1cey.bookshelf_pro_domain.OwnershipPolicy;
import org.jmolecules.ddd.annotation.AggregateRoot;
import org.jmolecules.ddd.annotation.Identity;

import jakarta.annotation.Nullable;
import jakarta.validation.Valid;

@AggregateRoot
public final class Account {

    @Identity
    private final AccountId id;
    @Valid
    private Username name;
    @Nullable
    @Valid
    private Email email;
    @Valid
    private Password password;

    Account(AccountId id, @Valid Username name, @Nullable @Valid Email email, @Valid Password password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public AccountId id() {
        return id;
    }

    public Username name() {
        return name;
    }

    // This is package private to be used in AccountService
    void changeName(@Valid Username newName, AccountId userRequestingChange) {
        OwnershipPolicy.validate(id, userRequestingChange, id);
        name = newName;
    }

    public Optional<Email> email() {
        return Optional.ofNullable(email);
    }

    public void changeEmail(@Valid Email newEmail, AccountId userRequestingChange) {
        OwnershipPolicy.validate(id, userRequestingChange, id);
        email = newEmail;
    }

    public Password password() {
        return password;
    }

    public void changePassword(@Valid Password newPassword, AccountId userRequestingChange) {
        OwnershipPolicy.validate(id, userRequestingChange, id);
        password = newPassword;
    }

}
