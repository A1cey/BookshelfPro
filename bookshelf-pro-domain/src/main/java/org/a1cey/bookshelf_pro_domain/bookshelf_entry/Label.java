package org.a1cey.bookshelf_pro_domain.bookshelf_entry;

import org.jmolecules.ddd.annotation.ValueObject;

import jakarta.validation.constraints.NotBlank;

@ValueObject
public record Label(@NotBlank String name) {

    public Label(@NotBlank String name) {
        if (name.isBlank()) {
            throw new IllegalArgumentException("Label name cannot be blank");
        }
        if (!name.chars().allMatch(c -> Character.isLowerCase(c) || Character.isDigit(c) || c == ' ')) {
            throw new IllegalArgumentException("Label name can only contain lowercase letters, digits and spaces");
        }

        this.name = normalizeLabel(name);
    }

    private static String normalizeLabel(String label) {
        return label.trim();
    }

}
