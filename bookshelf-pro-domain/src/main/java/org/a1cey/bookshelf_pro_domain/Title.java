package org.a1cey.bookshelf_pro_domain;

import org.jmolecules.ddd.annotation.ValueObject;

import jakarta.validation.constraints.NotBlank;

@ValueObject
public record Title(@NotBlank String title) {

    public Title {
        if (title.isBlank()) {
            throw new IllegalArgumentException("Title cannot be blank");
        }
    }

}
