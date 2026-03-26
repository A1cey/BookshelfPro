package org.a1cey.bookshelf_pro_domain.watchlist;

import org.a1cey.bookshelf_pro_domain.ID;
import org.jmolecules.ddd.annotation.ValueObject;

import java.util.UUID;

@ValueObject
public record WatchlistID(UUID value) implements ID {}
