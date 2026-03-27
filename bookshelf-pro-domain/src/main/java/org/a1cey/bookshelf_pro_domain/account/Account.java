package org.a1cey.bookshelf_pro_domain.account;

import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import org.a1cey.bookshelf_pro_domain.OwnershipPolicy;
import org.jmolecules.ddd.annotation.AggregateRoot;
import org.jmolecules.ddd.annotation.Identity;

import java.util.Optional;

@AggregateRoot
public final class Account {

    @Identity
    private final AccountID id;
    @Valid
    private Username name;
    @Nullable
    @Valid
    private Email email;
    @Valid
    private Password password;

    Account(AccountID id, @Valid Username name, @Nullable @Valid Email email, @Valid Password password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public AccountID id() {
        return id;
    }

    public Username name() {return name;}

    void changeName(@Valid Username newName, AccountID userRequestingChange) {
        OwnershipPolicy.validate(id, userRequestingChange, id);
        name = newName;
    }

    public Optional<Email> email() {
        return Optional.ofNullable(email);
    }

    public void changeEmail(@Valid Email newEmail, AccountID userRequestingChange) {
        OwnershipPolicy.validate(id, userRequestingChange, id);
        email = newEmail;
    }

    public Password password() {
        return password;
    }

    public void changePassword(@Valid Password newPassword, AccountID userRequestingChange) {
        OwnershipPolicy.validate(id, userRequestingChange, id);
        password = newPassword;
    }

}
