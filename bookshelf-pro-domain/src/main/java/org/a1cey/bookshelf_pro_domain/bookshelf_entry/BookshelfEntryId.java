package org.a1cey.bookshelf_pro_domain.bookshelf_entry;

import java.util.UUID;

import org.a1cey.bookshelf_pro_domain.Id;
import org.jmolecules.ddd.annotation.ValueObject;

@ValueObject
public record BookshelfEntryId(UUID value) implements Id {}
