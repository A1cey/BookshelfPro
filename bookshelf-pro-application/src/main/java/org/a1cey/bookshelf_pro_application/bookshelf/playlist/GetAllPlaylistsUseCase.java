package org.a1cey.bookshelf_pro_application.bookshelf.playlist;

import java.util.stream.Collectors;

import org.a1cey.bookshelf_pro_application.SecurityService;
import org.a1cey.bookshelf_pro_application.bookshelf.playlist.command.GetAllPlaylistsCommand;
import org.a1cey.bookshelf_pro_application.bookshelf.playlist.result.GetAllPlaylistsResult;
import org.a1cey.bookshelf_pro_application.dto.PlaylistDto;
import org.a1cey.bookshelf_pro_domain.bookshelf.playlist.PlaylistRepository;
import org.springframework.stereotype.Service;

@Service
public class GetAllPlaylistsUseCase {
    private final SecurityService securityService;
    private final PlaylistRepository playlistRepository;

    public GetAllPlaylistsUseCase(
        SecurityService securityService,
        PlaylistRepository playlistRepository
    ) {
        this.securityService = securityService;
        this.playlistRepository = playlistRepository;
    }

    public GetAllPlaylistsResult execute(GetAllPlaylistsCommand command) {
        var account = securityService.checkUser(command.owner(), command.name(), command.password());

        var playlists = playlistRepository
                            .findByOwner(account.id())
                            .stream()
                            .map(PlaylistDto::from)
                            .collect(Collectors.toSet());

        return new GetAllPlaylistsResult(playlists);
    }
}
