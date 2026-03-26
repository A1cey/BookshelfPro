package org.a1cey.bookshelf_pro_domain.bookshelf_entry.consumption;

import org.a1cey.bookshelf_pro_domain.ID;
import org.jmolecules.ddd.annotation.ValueObject;

import java.util.UUID;

@ValueObject
public record ConsumptionProgressID(UUID value) implements ID {}
