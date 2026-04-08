package org.a1cey.bookshelf_pro_domain.bookshelf.playlist;

import org.jmolecules.ddd.annotation.ValueObject;

@ValueObject
public record MovePlayListItem(PlaylistPosition oldPosition, PlaylistPosition newPosition) {}
