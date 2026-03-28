package org.a1cey.bookshelf_pro_domain.account;

import org.jmolecules.ddd.annotation.ValueObject;
import org.jspecify.annotations.NonNull;

import jakarta.validation.constraints.NotBlank;

@ValueObject
public record Username(@NonNull @NotBlank String name) {

    public Username {
        if (name.isBlank()) {
            throw new IllegalArgumentException("Username cannot be blank");
        }
        if (name.length() < 2) {
            throw new IllegalArgumentException("Username must be at least 2 characters long");
        }
        if (name.length() > 30) {
            throw new IllegalArgumentException("Username must be at most 30 characters long");
        }
        if (!name.chars().allMatch(c -> Character.isLetterOrDigit(c) || c == '_')) {
            throw new IllegalArgumentException("Username can only contain letters, digits and underscores");
        }
    }

}
