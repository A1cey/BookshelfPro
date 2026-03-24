package org.a1cey.bookshelf_pro_domain.label;

import jakarta.validation.constraints.NotBlank;
import org.jmolecules.ddd.annotation.ValueObject;

@ValueObject
public record Label(@NotBlank String name) {

    public Label {
        if (name.isBlank()) { // TODO: check label name constraints
            throw new IllegalArgumentException("Label name cannot be blank");
        }
    }

}
