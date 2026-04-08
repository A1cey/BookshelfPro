package org.a1cey.bookshelf_pro_domain.bookshelf.playlist;

import org.jmolecules.ddd.annotation.ValueObject;

import jakarta.validation.constraints.PositiveOrZero;

@ValueObject
public record PlaylistPosition(@PositiveOrZero int value) {
    public PlaylistPosition {
        if (value < 0) {
            throw new IllegalArgumentException("Playlist position cannot be less than 0");
        }
    }
}
