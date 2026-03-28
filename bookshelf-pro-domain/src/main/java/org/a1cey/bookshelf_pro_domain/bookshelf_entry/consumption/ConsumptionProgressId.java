package org.a1cey.bookshelf_pro_domain.bookshelf_entry.consumption;

import java.util.UUID;

import org.a1cey.bookshelf_pro_domain.Id;
import org.jmolecules.ddd.annotation.ValueObject;

@ValueObject
public record ConsumptionProgressId(UUID value) implements Id {}
