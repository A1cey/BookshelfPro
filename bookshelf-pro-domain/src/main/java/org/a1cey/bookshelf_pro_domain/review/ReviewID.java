package org.a1cey.bookshelf_pro_domain.review;

import org.a1cey.bookshelf_pro_domain.ID;
import org.jmolecules.ddd.annotation.ValueObject;

import java.util.UUID;

@ValueObject
public record ReviewID(UUID value) implements ID {
}
