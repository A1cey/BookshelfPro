package org.a1cey.bookshelf_pro_domain.review;

import org.jmolecules.ddd.annotation.ValueObject;

@ValueObject
public record Comment(String comment) {
}
