package org.a1cey.bookshelf_pro_domain.media_item;

import org.a1cey.bookshelf_pro_domain.ID;
import org.jmolecules.ddd.annotation.ValueObject;

@ValueObject
public record MediaItemID(ID id) {}
