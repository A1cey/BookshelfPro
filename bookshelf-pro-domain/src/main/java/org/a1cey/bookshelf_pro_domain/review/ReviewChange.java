package org.a1cey.bookshelf_pro_domain.review;

import java.time.LocalDateTime;

import org.a1cey.bookshelf_pro_domain.bookshelf_entry.consumption.ConsumptionProgressSnapshot;
import org.apache.commons.lang3.builder.HashCodeBuilder;
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

    public ReviewChange(Rating rating, Comment comment, LocalDateTime reviewDate, ConsumptionProgressSnapshot consumptionProgress) {
        this.rating = rating;
        this.comment = comment;
        this.reviewDate = reviewDate;
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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ReviewChange other)) {
            return false;
        }

        return rating.equals(other.rating())
                   && comment.equals(other.comment())
                   && reviewDate.equals(other.reviewDate())
                   && consumptionProgress.equals(other.consumptionProgress);
    }

    @Override
    public int hashCode() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        hashCodeBuilder.append(rating);
        hashCodeBuilder.append(comment);
        hashCodeBuilder.append(reviewDate);
        hashCodeBuilder.append(consumptionProgress);
        return hashCodeBuilder.toHashCode();
    }
}
