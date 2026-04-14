package org.a1cey.bookshelf_pro_domain.media_item.movie;

import java.time.LocalDate;

import org.jmolecules.ddd.annotation.ValueObject;

import jakarta.validation.constraints.PastOrPresent;

@ValueObject
public record ReleaseDate(@PastOrPresent LocalDate date) {

    public ReleaseDate {
        if (date.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Release date cannot be in the future");
        }
    }

}