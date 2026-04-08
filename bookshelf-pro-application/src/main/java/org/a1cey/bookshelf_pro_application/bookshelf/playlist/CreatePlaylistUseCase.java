package org.a1cey.bookshelf_pro_application.bookshelf.playlist;

import org.a1cey.bookshelf_pro_application.IdService;
import org.a1cey.bookshelf_pro_application.SecurityService;
import org.a1cey.bookshelf_pro_application.bookshelf.playlist.command.CreatePlaylistCommand;
import org.a1cey.bookshelf_pro_domain.bookshelf.playlist.Playlist;
import org.a1cey.bookshelf_pro_domain.bookshelf.playlist.PlaylistId;
import org.a1cey.bookshelf_pro_domain.bookshelf.playlist.PlaylistRepository;
import org.springframework.stereotype.Service;

@Service
public class CreatePlaylistUseCase {
    private final SecurityService securityService;
    private final PlaylistRepository playlistRepository;
    private final IdService idService;

    public CreatePlaylistUseCase(
        SecurityService securityService,
        PlaylistRepository playlistRepository,
        IdService idService
    ) {
        this.securityService = securityService;
        this.playlistRepository = playlistRepository;
        this.idService = idService;
    }

    public PlaylistId execute(CreatePlaylistCommand command) {
        var account = securityService.checkUser(command.owner(), command.name(), command.password());

        var playlistId = new PlaylistId(idService.generateId());

        var playlist = new Playlist(playlistId, account.id(), command.title(), command.itemsAsBookshelfEntryIds());

        playlistRepository.save(playlist);

        return playlistId;
    }
}
