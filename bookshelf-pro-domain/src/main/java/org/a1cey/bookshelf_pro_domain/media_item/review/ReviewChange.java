package org.a1cey.bookshelf_pro_domain.media_item.review;

import org.a1cey.bookshelf_pro_domain.consumption.MediaItemConsumptionProgress;
import org.jmolecules.ddd.annotation.ValueObject;

import java.util.Date;
import java.util.Optional;

@ValueObject
public class ReviewChange {

    private final Optional<Rating> rating;
    private final Optional<Comment> comment;
    private final Date reviewDate;
    private final MediaItemConsumptionProgress consumptionProgress;

    public ReviewChange(Optional<Rating> rating, Optional<Comment> comment, MediaItemConsumptionProgress consumptionProgress) {
        this.rating = rating;
        this.comment = comment;
        this.reviewDate = new Date();
        this.consumptionProgress = consumptionProgress;
    }

    public Optional<Rating> rating() {
        return rating;
    }

    public Optional<Comment> comment() {
        return comment;
    }

    public Date reviewDate() {
        return reviewDate;
    }

    public MediaItemConsumptionProgress consumptionProgress() {
        return consumptionProgress;
    }

}
