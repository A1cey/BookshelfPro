package org.a1cey.bookshelf_pro_application.dto;

import org.a1cey.bookshelf_pro_domain.media_item.movie.Actor;

public record ActorDto(
    String name,
    String role
) {

    public static ActorDto from(Actor actor) {
        return new ActorDto(actor.name(), actor.role());
    }
}
