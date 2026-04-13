package org.a1cey.bookshelf_pro_application.bookshelf.playlist;

import org.a1cey.bookshelf_pro_application.IdService;
import org.a1cey.bookshelf_pro_application.bookshelf.playlist.command.CreatePlaylistCommand;
import org.a1cey.bookshelf_pro_application.bookshelf.playlist.result.CreatePlaylistResult;
import org.a1cey.bookshelf_pro_application.security.CurrentUserProvider;
import org.a1cey.bookshelf_pro_domain.bookshelf.playlist.Playlist;
import org.a1cey.bookshelf_pro_domain.bookshelf.playlist.PlaylistId;
import org.a1cey.bookshelf_pro_domain.bookshelf.playlist.PlaylistRepository;
import org.springframework.stereotype.Service;

@Service
public class CreatePlaylistUseCase {
    private final PlaylistRepository playlistRepository;
    private final IdService idService;
    private final CurrentUserProvider currentUserProvider;

    public CreatePlaylistUseCase(
        PlaylistRepository playlistRepository,
        IdService idService,
        CurrentUserProvider currentUserProvider) {
        this.playlistRepository = playlistRepository;
        this.idService = idService;
        this.currentUserProvider = currentUserProvider;
    }

    public CreatePlaylistResult execute(CreatePlaylistCommand command) {
        var owner = currentUserProvider.currentUser();
        var playlistId = new PlaylistId(idService.generateId());

        var playlist = new Playlist(playlistId, owner.id(), command.title(), command.itemsAsBookshelfEntryIds());

        playlistRepository.save(playlist);

        return new CreatePlaylistResult(playlistId.value());
    }
}
