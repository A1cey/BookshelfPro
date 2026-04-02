package org.a1cey.bookshelf_pro_domain.media_item.review;

import java.util.UUID;

import org.a1cey.bookshelf_pro_domain.Id;
import org.jmolecules.ddd.annotation.ValueObject;

@ValueObject
public record ReviewId(UUID value) implements Id {
}
