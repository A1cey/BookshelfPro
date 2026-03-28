package org.a1cey.bookshelf_pro_domain.media_item.book;

import org.jmolecules.ddd.annotation.ValueObject;

import jakarta.validation.constraints.NotBlank;

@ValueObject
public record Author(@NotBlank String name) {

    public Author {
        if (name.isBlank()) {
            throw new IllegalArgumentException("Author name cannot be blank");
        }
    }

}