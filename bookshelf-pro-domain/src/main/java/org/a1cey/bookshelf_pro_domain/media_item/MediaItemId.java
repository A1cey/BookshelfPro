package org.a1cey.bookshelf_pro_domain.media_item;

import java.util.UUID;

import org.a1cey.bookshelf_pro_domain.Id;
import org.jmolecules.ddd.annotation.ValueObject;

@ValueObject
public record MediaItemId(UUID value) implements Id {}
