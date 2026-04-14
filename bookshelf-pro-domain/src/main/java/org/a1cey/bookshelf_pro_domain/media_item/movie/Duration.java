package org.a1cey.bookshelf_pro_domain.media_item.movie;

import org.jmolecules.ddd.annotation.ValueObject;

@ValueObject
public record Duration(java.time.Duration time) {
    public Duration {
        if (time.isNegative()) {
            throw new IllegalArgumentException("Duration cannot be negative");
        }
    }

    public static Duration of(int seconds) {
        return new Duration(java.time.Duration.ofSeconds(seconds));
    }
}
