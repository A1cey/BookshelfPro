package org.a1cey.bookshelf_pro_domain;

import jakarta.validation.constraints.NotBlank;
import org.jmolecules.ddd.annotation.ValueObject;

@ValueObject
public record Title(@NotBlank String title) {

    public Title {
        if (title.isBlank()) {
            throw new IllegalArgumentException("Title cannot be blank");
        }
    }

}
