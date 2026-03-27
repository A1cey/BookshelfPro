package org.a1cey.bookshelf_pro_domain.account;

import org.jmolecules.ddd.annotation.ValueObject;

@ValueObject
public record Password(String hashedPassword) {
    // No validation here, the password is already hashed
}
