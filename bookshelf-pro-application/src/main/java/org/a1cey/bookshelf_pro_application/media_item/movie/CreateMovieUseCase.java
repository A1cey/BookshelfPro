package org.a1cey.bookshelf_pro_application.media_item.movie;

import org.a1cey.bookshelf_pro_application.IdService;
import org.a1cey.bookshelf_pro_application.media_item.movie.command.CreateMovieCommand;
import org.a1cey.bookshelf_pro_application.media_item.movie.result.CreateMovieResult;
import org.a1cey.bookshelf_pro_application.security.CurrentUserProvider;
import org.a1cey.bookshelf_pro_domain.media_item.MediaItemId;
import org.a1cey.bookshelf_pro_domain.media_item.MediaItemRepository;
import org.a1cey.bookshelf_pro_domain.media_item.movie.Movie;
import org.springframework.stereotype.Service;

@Service
public final class CreateMovieUseCase {
    private final MediaItemRepository mediaItemRepository;
    private final IdService idService;
    private final CurrentUserProvider currentUserProvider;

    public CreateMovieUseCase(MediaItemRepository mediaItemRepository, IdService idService, CurrentUserProvider currentUserProvider) {
        this.mediaItemRepository = mediaItemRepository;
        this.idService = idService;
        this.currentUserProvider = currentUserProvider;
    }

    public CreateMovieResult execute(CreateMovieCommand command) {
        var owner = currentUserProvider.currentUser();
        var id = new MediaItemId(idService.generateId());

        var builder = Movie.builder(id, owner.id(), command.title(), command.imdbTitleId(), command.duration());

        command.actors().ifPresent(builder::actors);
        command.directors().ifPresent(builder::directors);
        command.studios().ifPresent(builder::studios);
        command.releaseDate().ifPresent(builder::releaseDate);
        command.originCountry().ifPresent(builder::originCountry);
        command.description().ifPresent(builder::description);
        command.subtitle().ifPresent(builder::subtitle);
        command.languages().ifPresent(builder::languages);
        command.coverImageUrl().ifPresent(builder::coverImageUrl);

        var movie = builder.build();

        mediaItemRepository.save(movie);

        return new CreateMovieResult(id);
    }
}
