package org.a1cey.bookshelf_pro_domain.book;

import jakarta.validation.constraints.NotBlank;
import org.jmolecules.ddd.annotation.ValueObject;

@ValueObject
public record Author(@NotBlank String name) {

    public Author {
        if (name.isBlank()) {
            throw new IllegalArgumentException("Author name cannot be blank");
        }
    }

}