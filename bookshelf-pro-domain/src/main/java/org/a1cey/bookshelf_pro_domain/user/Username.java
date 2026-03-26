package org.a1cey.bookshelf_pro_domain.user;

import org.jmolecules.ddd.annotation.ValueObject;
import org.jspecify.annotations.NonNull;

@ValueObject
public record Username(@NonNull String name) {
    //TODO: Validate username
}
