package org.a1cey.bookshelf_pro_application.bookshelf.playlist;

import java.util.Optional;

import org.a1cey.bookshelf_pro_application.SecurityService;
import org.a1cey.bookshelf_pro_application.bookshelf.playlist.command.GetPlaylistByIdCommand;
import org.a1cey.bookshelf_pro_application.bookshelf.playlist.result.GetPlaylistByIdResult;
import org.a1cey.bookshelf_pro_application.dto.PlaylistDto;
import org.a1cey.bookshelf_pro_domain.bookshelf.playlist.PlaylistRepository;
import org.springframework.stereotype.Service;

@Service
public class GetPlaylistByIdUseCase {
    private final SecurityService securityService;
    private final PlaylistRepository playlistRepository;

    public GetPlaylistByIdUseCase(
        SecurityService securityService,
        PlaylistRepository playlistRepository
    ) {
        this.securityService = securityService;
        this.playlistRepository = playlistRepository;
    }

    public Optional<GetPlaylistByIdResult> execute(GetPlaylistByIdCommand command) {
        var account = securityService.checkUser(command.owner(), command.name(), command.password());

        return playlistRepository
                   .findByIdAndOwner(command.playlistId(), account.id())
                   .map(PlaylistDto::from)
                   .map(GetPlaylistByIdResult::new);
    }
}
