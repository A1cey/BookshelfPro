package org.a1cey.bookshelf_pro_application.bookshelf.playlist;

import java.util.stream.Collectors;

import org.a1cey.bookshelf_pro_application.bookshelf.playlist.result.GetAllPlaylistsResult;
import org.a1cey.bookshelf_pro_application.dto.PlaylistDto;
import org.a1cey.bookshelf_pro_application.security.CurrentUserProvider;
import org.a1cey.bookshelf_pro_domain.bookshelf.playlist.PlaylistRepository;
import org.springframework.stereotype.Service;

@Service
public class GetAllPlaylistsUseCase {
    private final PlaylistRepository playlistRepository;
    private final CurrentUserProvider currentUserProvider;

    public GetAllPlaylistsUseCase(PlaylistRepository playlistRepository, CurrentUserProvider currentUserProvider) {
        this.playlistRepository = playlistRepository;
        this.currentUserProvider = currentUserProvider;
    }

    public GetAllPlaylistsResult execute() {
        var owner = currentUserProvider.currentUser();
        var playlists = playlistRepository
                            .findByOwner(owner.id())
                            .stream()
                            .map(PlaylistDto::from)
                            .collect(Collectors.toSet());

        return new GetAllPlaylistsResult(playlists);
    }
}
