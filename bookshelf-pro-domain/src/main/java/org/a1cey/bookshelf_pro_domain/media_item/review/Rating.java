package org.a1cey.bookshelf_pro_domain.media_item.review;

import jakarta.validation.constraints.PositiveOrZero;
import org.jmolecules.ddd.annotation.ValueObject;

@ValueObject
public record Rating(@PositiveOrZero float rating) {

    public Rating {
        rating = normalizeRating(rating);
    }

    /**
     * Normalizes a rating value to lay in the interval [0.0, 10.0] rounded to one decimal place.
     **/
    private static float normalizeRating(float rating) {
        var clampedRating = Math.clamp(rating, 0.0, 10.0);
        var normalizedRating = Math.round(clampedRating * 10.0) / 10.0;

        return (float) normalizedRating;
    }

}
