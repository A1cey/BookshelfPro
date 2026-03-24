package org.a1cey.bookshelf_pro_domain.media_item.book;

import jakarta.validation.constraints.PastOrPresent;
import org.jmolecules.ddd.annotation.ValueObject;

import java.time.LocalDate;

@ValueObject
public record PublishDate(@PastOrPresent LocalDate publishDate) {

    public PublishDate {
        if (publishDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Publish date cannot be in the future");
        }
    }

}
