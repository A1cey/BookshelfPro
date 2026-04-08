package org.a1cey.bookshelf_pro_plugins.rest.bookshelf.playlist;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.a1cey.bookshelf_pro_application.bookshelf.playlist.CreatePlaylistUseCase;
import org.a1cey.bookshelf_pro_application.bookshelf.playlist.DeletePlaylistUseCase;
import org.a1cey.bookshelf_pro_application.bookshelf.playlist.GetAllPlaylistsUseCase;
import org.a1cey.bookshelf_pro_application.bookshelf.playlist.GetPlaylistByIdUseCase;
import org.a1cey.bookshelf_pro_application.bookshelf.playlist.UpdatePlaylistUseCase;
import org.a1cey.bookshelf_pro_application.bookshelf.playlist.command.CreatePlaylistCommand;
import org.a1cey.bookshelf_pro_application.bookshelf.playlist.command.DeletePlaylistCommand;
import org.a1cey.bookshelf_pro_application.bookshelf.playlist.command.GetAllPlaylistsCommand;
import org.a1cey.bookshelf_pro_application.bookshelf.playlist.command.GetPlaylistByIdCommand;
import org.a1cey.bookshelf_pro_application.bookshelf.playlist.command.UpdatePlaylistCommand;
import org.a1cey.bookshelf_pro_application.bookshelf.playlist.result.GetAllPlaylistsResult;
import org.a1cey.bookshelf_pro_application.bookshelf.playlist.result.GetPlaylistByIdResult;
import org.a1cey.bookshelf_pro_domain.Title;
import org.a1cey.bookshelf_pro_domain.account.AccountId;
import org.a1cey.bookshelf_pro_domain.account.Password;
import org.a1cey.bookshelf_pro_domain.account.Username;
import org.a1cey.bookshelf_pro_domain.bookshelf.bookshelf_entry.BookshelfEntryId;
import org.a1cey.bookshelf_pro_domain.bookshelf.playlist.MovePlayListItem;
import org.a1cey.bookshelf_pro_domain.bookshelf.playlist.NewPlaylistItem;
import org.a1cey.bookshelf_pro_domain.bookshelf.playlist.PlaylistId;
import org.a1cey.bookshelf_pro_domain.bookshelf.playlist.PlaylistPosition;
import org.a1cey.bookshelf_pro_plugins.rest.Credentials;
import org.a1cey.bookshelf_pro_plugins.rest.bookshelf.playlist.request.CreatePlaylistRequest;
import org.a1cey.bookshelf_pro_plugins.rest.bookshelf.playlist.request.UpdatePlaylistRequest;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bookshelf/playlist")
public class PlaylistController {

    private final UpdatePlaylistUseCase updatePlaylistUseCase;
    private final CreatePlaylistUseCase createPlaylistUseCase;
    private final DeletePlaylistUseCase deletePlaylistUseCase;
    private final GetAllPlaylistsUseCase getAllPlaylistsUseCase;
    private final GetPlaylistByIdUseCase getPlaylistByIdUseCase;

    public PlaylistController(
        UpdatePlaylistUseCase updatePlaylistUseCase,
        CreatePlaylistUseCase createPlaylistUseCase,
        DeletePlaylistUseCase deletePlaylistUseCase,
        GetAllPlaylistsUseCase getAllPlaylistsUseCase,
        GetPlaylistByIdUseCase getPlaylistByIdUseCase
    ) {
        this.updatePlaylistUseCase = updatePlaylistUseCase;
        this.createPlaylistUseCase = createPlaylistUseCase;
        this.deletePlaylistUseCase = deletePlaylistUseCase;
        this.getAllPlaylistsUseCase = getAllPlaylistsUseCase;
        this.getPlaylistByIdUseCase = getPlaylistByIdUseCase;
    }

    @PostMapping
    public void createPlaylist(
        @ParameterObject Credentials credentials,
        @RequestBody CreatePlaylistRequest request
    ) {
        createPlaylistUseCase.execute(new CreatePlaylistCommand(
            new AccountId(credentials.accountId()),
            new Username(credentials.username()),
            new Password(credentials.password()),
            new Title(request.title()),
            request.itemsAsBookshelfEntryIds().stream().map(BookshelfEntryId::new).toList()
        ));
    }

    @PatchMapping("/{playlistId}")
    public void updatePlaylist(
        @PathVariable UUID playlistId,
        @ParameterObject Credentials credentials,
        @RequestBody UpdatePlaylistRequest request
    ) {
        var newItems = Optional
                           .ofNullable(request.newItems())
                           .map(items -> items
                                             .stream()
                                             .map(item -> new NewPlaylistItem(
                                                 new BookshelfEntryId(item.item()),
                                                 Optional.ofNullable(item.position()).map(PlaylistPosition::new)
                                             ))
                                             .toList()
                           );

        var moveItems = Optional
                            .ofNullable(request.moveItems())
                            .map(items -> items
                                              .stream()
                                              .map(item -> new MovePlayListItem(
                                                  new PlaylistPosition(item.oldPosition()), new PlaylistPosition(item.newPosition())
                                              )).collect(Collectors.toSet())
                            );

        updatePlaylistUseCase.execute(new UpdatePlaylistCommand(
            new AccountId(credentials.accountId()),
            new Username(credentials.username()),
            new Password(credentials.password()),
            new PlaylistId(playlistId),
            Optional.ofNullable(request.newTitle()).map(Title::new),
            newItems,
            Optional.ofNullable(request.removeItemsFromPositions())
                    .map(items -> items.stream().map(PlaylistPosition::new).collect(Collectors.toSet())),
            moveItems
        ));
    }

    @DeleteMapping("/{playlistId}")
    public void deletePlaylist(@PathVariable UUID playlistId, @ParameterObject Credentials credentials) {
        deletePlaylistUseCase.execute(new DeletePlaylistCommand(
            new AccountId(credentials.accountId()),
            new Username(credentials.username()),
            new Password(credentials.password()),
            new PlaylistId(playlistId)
        ));
    }

    @GetMapping
    public ResponseEntity<GetAllPlaylistsResult> getAllPlaylists(@ParameterObject Credentials credentials) {
        return ResponseEntity.ok(getAllPlaylistsUseCase.execute(new GetAllPlaylistsCommand(
            new AccountId(credentials.accountId()),
            new Username(credentials.username()),
            new Password(credentials.password())
        )));
    }

    @GetMapping("/{playlistId}")
    public ResponseEntity<GetPlaylistByIdResult> getPlaylistById(
        @PathVariable UUID playlistId, @ParameterObject Credentials credentials
    ) {
        return ResponseEntity.of(getPlaylistByIdUseCase.execute(new GetPlaylistByIdCommand(
            new AccountId(credentials.accountId()),
            new Username(credentials.username()),
            new Password(credentials.password()),
            new PlaylistId(playlistId)
        )));
    }
}
