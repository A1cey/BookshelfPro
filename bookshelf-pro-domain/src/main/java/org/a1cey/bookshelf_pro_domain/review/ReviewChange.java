package org.a1cey.bookshelf_pro_domain.review;

import java.time.LocalDateTime;

import org.a1cey.bookshelf_pro_domain.bookshelf_entry.consumption.ConsumptionProgressSnapshot;
import org.jmolecules.ddd.annotation.ValueObject;

import jakarta.validation.Valid;

@ValueObject
public final class ReviewChange {

    @Valid
    private final Rating rating;
    private final Comment comment;
    private final LocalDateTime reviewDate;
    private final ConsumptionProgressSnapshot consumptionProgress;

    public ReviewChange(Rating rating, Comment comment, ConsumptionProgressSnapshot consumptionProgress) {
        this.rating = rating;
        this.comment = comment;
        this.reviewDate = LocalDateTime.now();
        this.consumptionProgress = consumptionProgress;
    }

    public Rating rating() {
        return rating;
    }

    public Comment comment() {
        return comment;
    }

    public LocalDateTime reviewDate() {
        return reviewDate;
    }

    public ConsumptionProgressSnapshot consumptionProgress() {
        return consumptionProgress;
    }

}
