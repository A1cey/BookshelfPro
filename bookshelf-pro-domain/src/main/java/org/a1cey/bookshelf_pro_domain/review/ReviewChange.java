package org.a1cey.bookshelf_pro_domain.review;

import org.a1cey.bookshelf_pro_domain.bookshelf_entry.consumption.MediaItemConsumptionProgress;
import org.jmolecules.ddd.annotation.ValueObject;

import java.util.Date;

@ValueObject
public class ReviewChange {

    private final Rating rating;
    private final Comment comment;
    private final Date reviewDate;
    private final MediaItemConsumptionProgress consumptionProgress;

    public ReviewChange(Rating rating, Comment comment, MediaItemConsumptionProgress consumptionProgress) {
        this.rating = rating;
        this.comment = comment;
        this.reviewDate = new Date();
        this.consumptionProgress = consumptionProgress;
    }

    public Rating rating() {
        return rating;
    }

    public Comment comment() {
        return comment;
    }

    public Date reviewDate() {
        return reviewDate;
    }

    public MediaItemConsumptionProgress consumptionProgress() {
        return consumptionProgress;
    }

}
