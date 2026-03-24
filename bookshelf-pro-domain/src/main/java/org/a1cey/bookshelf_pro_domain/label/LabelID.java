package org.a1cey.bookshelf_pro_domain.label;

import org.a1cey.bookshelf_pro_domain.ID;
import org.jmolecules.ddd.annotation.ValueObject;

@ValueObject
public record LabelID(ID id) {}
