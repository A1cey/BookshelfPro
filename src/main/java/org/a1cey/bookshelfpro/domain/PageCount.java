package org.a1cey.bookshelfpro.domain;

import jakarta.validation.constraints.PositiveOrZero;
import org.jmolecules.ddd.annotation.ValueObject;

@ValueObject
public record PageCount(@PositiveOrZero int pageCount) {

    public PageCount {
        if (pageCount < 0) {
            throw new IllegalArgumentException("Page count must be a positive integer");
        }
    }

}
