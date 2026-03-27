package org.a1cey.bookshelf_pro_domain.account;

import jakarta.validation.constraints.NotBlank;
import org.jmolecules.ddd.annotation.ValueObject;

@ValueObject
public record Password(@NotBlank String hashedPassword) {

    public Password {
        if (hashedPassword.isBlank()) {
            throw new IllegalArgumentException("Password cannot be blank");
        }
    }

}
