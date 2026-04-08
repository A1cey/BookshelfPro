package org.a1cey.bookshelf_pro_application.bookshelf.playlist;

import org.a1cey.bookshelf_pro_application.SecurityService;
import org.a1cey.bookshelf_pro_application.bookshelf.playlist.command.UpdatePlaylistCommand;
import org.a1cey.bookshelf_pro_domain.bookshelf.bookshelf_entry.BookshelfEntryRepository;
import org.a1cey.bookshelf_pro_domain.bookshelf.playlist.PlaylistRepository;
import org.springframework.stereotype.Service;

@Service
public class UpdatePlaylistUseCase {
    private final SecurityService securityService;
    private final org.a1cey.bookshelf_pro_domain.bookshelf.playlist.PlaylistRepository playlistRepository;
    private final BookshelfEntryRepository bookshelfEntryRepository;

    public UpdatePlaylistUseCase(
        SecurityService securityService,
        PlaylistRepository playlistRepository,
        BookshelfEntryRepository bookshelfEntryRepository
    ) {
        this.securityService = securityService;
        this.playlistRepository = playlistRepository;
        this.bookshelfEntryRepository = bookshelfEntryRepository;
    }

    public void execute(UpdatePlaylistCommand command) {
        var account = securityService.checkUser(command.owner(), command.name(), command.password());

        var playlist = playlistRepository
                           .findById(command.playlistId())
                           .orElseThrow(
                               () -> new IllegalArgumentException("Playlist with id " + command.playlistId().value() + " not found")
                           );

        if (command.newTitle().isPresent()) {
            playlist.changeTitle(command.newTitle().get(), account.id());
        }

        command
            .newItems()
            .ifPresent(
                items ->
                    items.forEach(newPlaylistItem -> {
                        if (!bookshelfEntryRepository.existsByAccountAndId(account.id(), newPlaylistItem.item())) {
                            throw new IllegalArgumentException(
                                "Bookshelf entry with id " + newPlaylistItem + " not found for user"
                            );
                        }
                        playlist.addItem(newPlaylistItem, account.id());
                    })
            );

        command
            .removeItemsFromPositions()
            .ifPresent(
                positions ->
                    positions.forEach(position -> {
                        if (playlist.items().size() <= position.value()) {
                            throw new IllegalArgumentException("Position " + position + " greater than playlist length");
                        }
                        playlist.removeItem(position, account.id());
                    }));

        command
            .moveItems()
            .ifPresent(moveItems ->
                           moveItems.forEach(moveItem -> playlist.moveItem(moveItem, account.id()))
            );

        playlistRepository.update(playlist);
    }
}
