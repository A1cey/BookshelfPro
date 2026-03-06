package org.a1cey.bookshelfpro.domain;

import jakarta.validation.constraints.NotBlank;
import org.jmolecules.ddd.annotation.ValueObject;

@ValueObject
public record Label(@NotBlank String name) {

    public Label {
        if (name.isBlank()) {
            throw new IllegalArgumentException("Label name cannot be blank");
        }
    }

}
