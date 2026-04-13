package org.a1cey.bookshelf_pro_application.bookshelf.playlist;

import org.a1cey.bookshelf_pro_application.bookshelf.playlist.command.DeletePlaylistCommand;
import org.a1cey.bookshelf_pro_application.security.CurrentUserProvider;
import org.a1cey.bookshelf_pro_domain.bookshelf.playlist.PlaylistRepository;
import org.springframework.stereotype.Service;

@Service
public class DeletePlaylistUseCase {
    private final PlaylistRepository playlistRepository;
    private final CurrentUserProvider currentUserProvider;

    public DeletePlaylistUseCase(PlaylistRepository playlistRepository, CurrentUserProvider currentUserProvider) {
        this.playlistRepository = playlistRepository;
        this.currentUserProvider = currentUserProvider;
    }

    public void execute(DeletePlaylistCommand command) {
        var owner = currentUserProvider.currentUser();
        var playlist = playlistRepository
                           .findById(command.playlistId())
                           .orElseThrow(
                               () -> new IllegalArgumentException("Playlist with id " + command.playlistId().value() + " not found")
                           );

        if (!playlist.owner().equals(owner.id())) {
            throw new SecurityException("User is not the owner of this playlist");
        }

        playlistRepository.delete(playlist);
    }
}
