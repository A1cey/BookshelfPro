package org.a1cey.bookshelf_pro_domain.bookshelf.playlist;

import org.jmolecules.ddd.annotation.ValueObject;
import org.jspecify.annotations.NonNull;

import jakarta.validation.constraints.PositiveOrZero;

@ValueObject
public record PlaylistPosition(@PositiveOrZero int value) implements Comparable<PlaylistPosition> {
    public PlaylistPosition {
        if (value < 0) {
            throw new IllegalArgumentException("Playlist position cannot be less than 0");
        }
    }

    @Override
    public int compareTo(@NonNull PlaylistPosition other) {
        return Integer.compare(value, other.value);
    }
}
