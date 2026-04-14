package org.a1cey.bookshelf_pro_application.media_item.movie.command;

import org.a1cey.bookshelf_pro_domain.media_item.MediaItemId;

public record GetMovieCommand(MediaItemId movieId) {}
