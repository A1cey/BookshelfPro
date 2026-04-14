package org.a1cey.bookshelf_pro_domain.media_item.book;

import org.apache.commons.validator.routines.ISBNValidator;
import org.jmolecules.ddd.annotation.ValueObject;

import jakarta.validation.constraints.NotBlank;

@ValueObject
public record Isbn(@NotBlank @org.hibernate.validator.constraints.ISBN String value) {

    private static final ISBNValidator validator = ISBNValidator.getInstance();

    public Isbn(@NotBlank @org.hibernate.validator.constraints.ISBN String value) {
        if (value.isBlank()) {
            throw new IllegalArgumentException("ISBN time cannot be blank");
        }
        String validIsbn = validator.validate(value);
        if (validIsbn == null) {
            throw new IllegalArgumentException("Invalid ISBN: " + value);
        }

        this.value = validIsbn;
    }

}

