package org.a1cey.bookshelf_pro_domain.media_item.review;

import java.time.LocalDateTime;

import org.a1cey.bookshelf_pro_domain.bookshelf.bookshelf_entry.consumption.ConsumptionProgressSnapshot;
import org.jmolecules.ddd.annotation.ValueObject;

import jakarta.validation.Valid;

@ValueObject
public record ReviewChange(@Valid Rating rating, Comment comment, LocalDateTime reviewDate,
                           ConsumptionProgressSnapshot consumptionProgressSnapshot) {

    public ReviewChange(Rating rating, Comment comment, ConsumptionProgressSnapshot consumptionProgress) {
        this(rating, comment, LocalDateTime.now(), consumptionProgress);
    }

    public ReviewChange(Rating rating, Comment comment, LocalDateTime reviewDate, ConsumptionProgressSnapshot consumptionProgressSnapshot) {
        this.rating = rating;
        this.comment = comment;
        this.reviewDate = reviewDate;
        this.consumptionProgressSnapshot = consumptionProgressSnapshot;
    }

    @Override
    public Rating rating() {
        return rating;
    }
}
