package org.a1cey.bookshelf_pro_domain.media_item.book;

import jakarta.validation.constraints.NotBlank;
import org.apache.commons.validator.routines.ISBNValidator;
import org.jmolecules.ddd.annotation.ValueObject;

@ValueObject
public record ISBN(@NotBlank @org.hibernate.validator.constraints.ISBN String value) {

    private static final ISBNValidator validator = ISBNValidator.getInstance();

    public ISBN {
        if (value.isBlank()) {
            throw new IllegalArgumentException("ISBN value cannot be blank");
        }
        String validISBN = validator.validate(value);
        if (validISBN == null) {
            throw new IllegalArgumentException("Invalid ISBN: " + value);
        }
    }

}

