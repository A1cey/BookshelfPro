package org.a1cey.bookshelf_pro_domain.bookshelf.playlist;

import java.util.UUID;

import org.a1cey.bookshelf_pro_domain.Id;
import org.jmolecules.ddd.annotation.ValueObject;

@ValueObject
public record PlaylistId(UUID value) implements Id {}
