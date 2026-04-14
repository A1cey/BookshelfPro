package org.a1cey.bookshelf_pro_domain.media_item.movie;

import org.jmolecules.ddd.annotation.ValueObject;

import jakarta.validation.constraints.NotBlank;

@ValueObject
public record Studio(@NotBlank String name) {
}
