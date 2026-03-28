package org.a1cey.bookshelf_pro_domain.media_item.book;

import org.jmolecules.ddd.annotation.ValueObject;

import jakarta.validation.constraints.PositiveOrZero;

@ValueObject
public record PageCount(@PositiveOrZero int pageCount) {

    public PageCount {
        if (pageCount < 0) {
            throw new IllegalArgumentException("Page count must be a positive integer");
        }
    }

}
