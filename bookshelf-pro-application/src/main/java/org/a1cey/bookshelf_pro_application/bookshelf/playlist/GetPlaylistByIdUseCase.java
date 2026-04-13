package org.a1cey.bookshelf_pro_application.bookshelf.playlist;

import java.util.Optional;

import org.a1cey.bookshelf_pro_application.bookshelf.playlist.command.GetPlaylistByIdCommand;
import org.a1cey.bookshelf_pro_application.bookshelf.playlist.result.GetPlaylistByIdResult;
import org.a1cey.bookshelf_pro_application.dto.PlaylistDto;
import org.a1cey.bookshelf_pro_application.security.CurrentUserProvider;
import org.a1cey.bookshelf_pro_domain.bookshelf.playlist.PlaylistRepository;
import org.springframework.stereotype.Service;

@Service
public class GetPlaylistByIdUseCase {
    private final PlaylistRepository playlistRepository;
    private final CurrentUserProvider currentUserProvider;

    public GetPlaylistByIdUseCase(PlaylistRepository playlistRepository, CurrentUserProvider currentUserProvider) {
        this.playlistRepository = playlistRepository;
        this.currentUserProvider = currentUserProvider;
    }

    public Optional<GetPlaylistByIdResult> execute(GetPlaylistByIdCommand command) {
        var owner = currentUserProvider.currentUser();
        return playlistRepository
                   .findByIdAndOwner(command.playlistId(), owner.id())
                   .map(PlaylistDto::from)
                   .map(GetPlaylistByIdResult::new);
    }
}
