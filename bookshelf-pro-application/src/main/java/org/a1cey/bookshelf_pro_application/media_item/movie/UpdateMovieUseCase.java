package org.a1cey.bookshelf_pro_application.media_item.movie;

import org.a1cey.bookshelf_pro_application.media_item.movie.command.UpdateMovieCommand;
import org.a1cey.bookshelf_pro_application.security.CurrentUserProvider;
import org.a1cey.bookshelf_pro_domain.media_item.MediaItemRepository;
import org.a1cey.bookshelf_pro_domain.media_item.movie.Movie;
import org.springframework.stereotype.Service;

@Service
public final class UpdateMovieUseCase {

    private final MediaItemRepository mediaItemRepository;
    private final CurrentUserProvider currentUserProvider;

    public UpdateMovieUseCase(MediaItemRepository mediaItemRepository, CurrentUserProvider currentUserProvider) {
        this.mediaItemRepository = mediaItemRepository;
        this.currentUserProvider = currentUserProvider;
    }

    public void execute(UpdateMovieCommand command) {
        var owner = currentUserProvider.currentUser();
        var mediaItem = mediaItemRepository
                            .findById(command.movieId())
                            .orElseThrow(() -> new IllegalArgumentException("Movie with id " + command.movieId() + " not found"));

        if (!mediaItem.owner().equals(owner.id())) {
            throw new SecurityException("User not allowed to perform this action as they do not own the media item");
        }

        if (!(mediaItem instanceof Movie movie)) {
            throw new IllegalArgumentException("Movie id does not match a movie but another media item type: " + mediaItem.type());
        }

        command.title().ifPresent(newTitle -> movie.changeTitle(newTitle, owner.id()));
        command.subtitle().ifPresent(subtitle -> movie.changeSubtitle(subtitle, owner.id()));
        command.description().ifPresent(newDescription -> movie.changeDescription(newDescription, owner.id()));
        command.coverImageUrl().ifPresent(newCoverImageUrl -> movie.changeCoverImageUrl(newCoverImageUrl, owner.id()));
        command.languages().ifPresent(newLanguages -> movie.changeLanguages(newLanguages, owner.id()));
        command.actors().ifPresent(newActors -> movie.changeActors(newActors, owner.id()));
        command.directors().ifPresent(newDirectors -> movie.changeDirectors(newDirectors, owner.id()));
        command.studios().ifPresent(newStudios -> movie.changeStudios(newStudios, owner.id()));
        command.releaseDate().ifPresent(newReleaseDate -> movie.changeReleaseDate(newReleaseDate, owner.id()));
        command.originCountry().ifPresent(newOriginCountry -> movie.changeOriginCountry(newOriginCountry, owner.id()));
        command.duration().ifPresent(newDuration -> movie.changeDuration(newDuration, owner.id()));

        mediaItemRepository.update(movie);
    }
}
