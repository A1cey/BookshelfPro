package org.a1cey.bookshelfpro.domain;

import jakarta.validation.constraints.NotBlank;
import org.apache.commons.validator.routines.ISBNValidator;
import org.jmolecules.ddd.annotation.ValueObject;

@ValueObject
public record ISBN(@NotBlank @org.hibernate.validator.constraints.ISBN String value) {

    public ISBN {
        ISBNValidator validator = ISBNValidator.getInstance(); // singleton instance
        String validISBN = validator.validate(value);
        if (validISBN == null) {
            throw new IllegalArgumentException("Invalid ISBN: " + value);
        }
        value = validISBN;
    }

}