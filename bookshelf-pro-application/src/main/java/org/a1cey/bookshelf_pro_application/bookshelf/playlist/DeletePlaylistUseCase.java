package org.a1cey.bookshelf_pro_application.bookshelf.playlist;

import org.a1cey.bookshelf_pro_application.SecurityService;
import org.a1cey.bookshelf_pro_application.bookshelf.playlist.command.DeletePlaylistCommand;
import org.a1cey.bookshelf_pro_domain.bookshelf.playlist.PlaylistRepository;
import org.springframework.stereotype.Service;

@Service
public class DeletePlaylistUseCase {
    private final SecurityService securityService;
    private final PlaylistRepository playlistRepository;

    public DeletePlaylistUseCase(
        SecurityService securityService,
        PlaylistRepository playlistRepository
    ) {
        this.securityService = securityService;
        this.playlistRepository = playlistRepository;
    }

    public void execute(DeletePlaylistCommand command) {
        var account = securityService.checkUser(command.owner(), command.name(), command.password());

        var playlist = playlistRepository
                           .findById(command.playlistId())
                           .orElseThrow(
                               () -> new IllegalArgumentException("Playlist with id " + command.playlistId().value() + " not found")
                           );

        if (!playlist.owner().equals(account.id())) {
            throw new SecurityException("User is not the owner of this playlist");
        }

        playlistRepository.delete(playlist);
    }
}
