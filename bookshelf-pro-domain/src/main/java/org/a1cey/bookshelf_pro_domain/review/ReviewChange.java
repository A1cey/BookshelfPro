package org.a1cey.bookshelf_pro_domain.review;

import jakarta.validation.Valid;
import org.a1cey.bookshelf_pro_domain.bookshelf_entry.consumption.MediaItemConsumptionProgress;
import org.jmolecules.ddd.annotation.ValueObject;

import java.time.LocalDateTime;

@ValueObject
public final class ReviewChange {

    @Valid
    private final Rating rating;
    private final Comment comment;
    private final LocalDateTime reviewDate;
    private final MediaItemConsumptionProgress consumptionProgress;

    public ReviewChange(Rating rating, Comment comment, MediaItemConsumptionProgress consumptionProgress) {
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

    public MediaItemConsumptionProgress consumptionProgress() {
        return consumptionProgress;
    }

}
