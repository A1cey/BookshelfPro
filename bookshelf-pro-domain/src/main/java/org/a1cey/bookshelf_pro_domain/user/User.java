package org.a1cey.bookshelf_pro_domain.user;

import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import org.a1cey.bookshelf_pro_domain.OwnershipPolicy;
import org.jmolecules.ddd.annotation.AggregateRoot;
import org.jmolecules.ddd.annotation.Identity;

import java.util.Optional;

@AggregateRoot
public final class User {

    @Identity
    private final UserID id;
    @Valid
    private Username name;
    @Nullable
    @Valid
    private Email email;

    private User(UserID id, @Valid Username name, @Nullable @Valid Email email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public UserID id() {
        return id;
    }

    public Username name() {return name;}

    public void changeName(Username newName, UserID userRequestingChange) {
        OwnershipPolicy.validate(id, userRequestingChange, id);
        name = newName;
    }

    public Optional<Email> email() {
        return Optional.ofNullable(email);
    }

    public void changeEmail(@Valid Email newEmail, UserID userRequestingChange) {
        OwnershipPolicy.validate(id, userRequestingChange, id);
        email = newEmail;
    }

}
