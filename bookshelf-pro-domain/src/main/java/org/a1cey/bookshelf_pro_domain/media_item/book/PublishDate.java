package org.a1cey.bookshelf_pro_domain.media_item.book;

import java.time.LocalDate;

import org.jmolecules.ddd.annotation.ValueObject;

import jakarta.validation.constraints.PastOrPresent;

@ValueObject
public record PublishDate(@PastOrPresent LocalDate publishDate) {

    public PublishDate {
        if (publishDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Publish date cannot be in the future");
        }
    }

}
