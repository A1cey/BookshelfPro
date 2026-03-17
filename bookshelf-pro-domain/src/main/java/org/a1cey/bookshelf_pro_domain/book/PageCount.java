package org.a1cey.bookshelf_pro_domain.book;

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
