package org.a1cey.bookshelf_pro_application.bookshelf.playlist;

import org.a1cey.bookshelf_pro_application.bookshelf.playlist.command.UpdatePlaylistCommand;
import org.a1cey.bookshelf_pro_application.security.CurrentUserProvider;
import org.a1cey.bookshelf_pro_domain.bookshelf.bookshelf_entry.BookshelfEntryRepository;
import org.a1cey.bookshelf_pro_domain.bookshelf.playlist.PlaylistRepository;
import org.springframework.stereotype.Service;

@Service
public class UpdatePlaylistUseCase {
    private final org.a1cey.bookshelf_pro_domain.bookshelf.playlist.PlaylistRepository playlistRepository;
    private final BookshelfEntryRepository bookshelfEntryRepository;
    private final CurrentUserProvider currentUserProvider;

    public UpdatePlaylistUseCase(
        PlaylistRepository playlistRepository,
        BookshelfEntryRepository bookshelfEntryRepository,
        CurrentUserProvider currentUserProvider) {
        this.playlistRepository = playlistRepository;
        this.bookshelfEntryRepository = bookshelfEntryRepository;
        this.currentUserProvider = currentUserProvider;
    }

    public void execute(UpdatePlaylistCommand command) {
        var owner = currentUserProvider.currentUser();
        var playlist = playlistRepository
                           .findById(command.playlistId())
                           .orElseThrow(
                               () -> new IllegalArgumentException("Playlist with id " + command.playlistId().value() + " not found")
                           );

        if (command.newTitle().isPresent()) {
            playlist.changeTitle(command.newTitle().get(), owner.id());
        }

        command
            .newItems()
            .ifPresent(
                items ->
                    items.forEach(newPlaylistItem -> {
                        if (!bookshelfEntryRepository.existsByAccountAndId(owner.id(), newPlaylistItem.item())) {
                            throw new IllegalArgumentException(
                                "Bookshelf entry with id " + newPlaylistItem + " not found for user"
                            );
                        }
                        playlist.addItem(newPlaylistItem, owner.id());
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
                        playlist.removeItem(position, owner.id());
                    }));

        command
            .moveItems()
            .ifPresent(moveItems ->
                           moveItems.forEach(moveItem -> playlist.moveItem(moveItem, owner.id()))
            );

        playlistRepository.update(playlist);
    }
}
