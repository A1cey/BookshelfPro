package org.a1cey.bookshelf_pro_domain.consumption;

import org.a1cey.bookshelf_pro_domain.ID;
import org.jmolecules.ddd.annotation.ValueObject;

@ValueObject
public record ConsumptionProgressID(ID id) {}
