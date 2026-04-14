package org.a1cey.bookshelf_pro_domain.media_item.movie;

import jakarta.validation.constraints.NotBlank;

public record Director(@NotBlank String name) {}
