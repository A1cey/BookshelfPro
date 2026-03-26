package org.a1cey.bookshelf_pro_domain.playlist;

import org.a1cey.bookshelf_pro_domain.ID;
import org.jmolecules.ddd.annotation.ValueObject;

import java.util.UUID;

@ValueObject
public record PlaylistID(UUID value) implements ID {}
