package org.a1cey.bookshelf_pro_domain;

import jakarta.validation.constraints.Positive;
import org.jmolecules.ddd.annotation.ValueObject;

@ValueObject
public record ID(@Positive int id) {

    public ID {
        if (id <= 0) {
            throw new IllegalArgumentException("ID must be a positive integer");
        }
    }

}
