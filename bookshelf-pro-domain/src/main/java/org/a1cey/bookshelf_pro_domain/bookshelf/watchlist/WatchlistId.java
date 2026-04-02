package org.a1cey.bookshelf_pro_domain.bookshelf.watchlist;

import java.util.UUID;

import org.a1cey.bookshelf_pro_domain.Id;
import org.jmolecules.ddd.annotation.ValueObject;

@ValueObject
public record WatchlistId(UUID value) implements Id {}
