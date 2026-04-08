package org.a1cey.bookshelf_pro_plugins.rest.bookshelf.watchlist;

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.a1cey.bookshelf_pro_application.bookshelf.watchlist.CreateWatchlistUseCase;
import org.a1cey.bookshelf_pro_application.bookshelf.watchlist.DeleteWatchlistUseCase;
import org.a1cey.bookshelf_pro_application.bookshelf.watchlist.GetAllWatchlistsUseCase;
import org.a1cey.bookshelf_pro_application.bookshelf.watchlist.GetWatchlistByIdUseCase;
import org.a1cey.bookshelf_pro_application.bookshelf.watchlist.RemoveWatchlistItemsByConsumptionStateUseCase;
import org.a1cey.bookshelf_pro_application.bookshelf.watchlist.UpdateWatchlistUseCase;
import org.a1cey.bookshelf_pro_application.bookshelf.watchlist.command.CreateWatchlistCommand;
import org.a1cey.bookshelf_pro_application.bookshelf.watchlist.command.DeleteWatchlistCommand;
import org.a1cey.bookshelf_pro_application.bookshelf.watchlist.command.GetAllWatchlistsCommand;
import org.a1cey.bookshelf_pro_application.bookshelf.watchlist.command.GetWatchlistByIdCommand;
import org.a1cey.bookshelf_pro_application.bookshelf.watchlist.command.RemoveWatchlistItemsByConsumptionStateCommand;
import org.a1cey.bookshelf_pro_application.bookshelf.watchlist.command.UpdateWatchlistCommand;
import org.a1cey.bookshelf_pro_application.bookshelf.watchlist.result.GetAllWatchlistsResult;
import org.a1cey.bookshelf_pro_application.bookshelf.watchlist.result.GetWatchlistByIdResult;
import org.a1cey.bookshelf_pro_domain.Title;
import org.a1cey.bookshelf_pro_domain.account.AccountId;
import org.a1cey.bookshelf_pro_domain.account.Password;
import org.a1cey.bookshelf_pro_domain.account.Username;
import org.a1cey.bookshelf_pro_domain.bookshelf.bookshelf_entry.BookshelfEntryId;
import org.a1cey.bookshelf_pro_domain.bookshelf.watchlist.WatchlistId;
import org.a1cey.bookshelf_pro_plugins.rest.Credentials;
import org.a1cey.bookshelf_pro_plugins.rest.bookshelf.watchlist.request.CreateWatchlistRequest;
import org.a1cey.bookshelf_pro_plugins.rest.bookshelf.watchlist.request.RemoveWatchlistItemsByConsumptionStateRequest;
import org.a1cey.bookshelf_pro_plugins.rest.bookshelf.watchlist.request.UpdateWatchlistRequest;
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
@RequestMapping("/bookshelf/watchlist")
public class WatchlistController {

    private final UpdateWatchlistUseCase updateWatchlistUseCase;
    private final CreateWatchlistUseCase createWatchlistUseCase;
    private final DeleteWatchlistUseCase deleteWatchlistUseCase;
    private final GetAllWatchlistsUseCase getAllWatchlistsUseCase;
    private final GetWatchlistByIdUseCase getWatchlistByIdUseCase;
    private final RemoveWatchlistItemsByConsumptionStateUseCase removeWatchlistItemsByConsumptionStateUseCase;

    public WatchlistController(UpdateWatchlistUseCase updateWatchlistUseCase, CreateWatchlistUseCase createWatchlistUseCase,
                               DeleteWatchlistUseCase deleteWatchlistUseCase, GetAllWatchlistsUseCase getAllWatchlistsUseCase,
                               GetWatchlistByIdUseCase getWatchlistByIdUseCase,
                               RemoveWatchlistItemsByConsumptionStateUseCase removeWatchlistItemsByConsumptionStateUseCase) {
        this.updateWatchlistUseCase = updateWatchlistUseCase;
        this.createWatchlistUseCase = createWatchlistUseCase;
        this.deleteWatchlistUseCase = deleteWatchlistUseCase;
        this.getAllWatchlistsUseCase = getAllWatchlistsUseCase;
        this.getWatchlistByIdUseCase = getWatchlistByIdUseCase;
        this.removeWatchlistItemsByConsumptionStateUseCase = removeWatchlistItemsByConsumptionStateUseCase;
    }

    @PostMapping
    public void createWatchlist(
        @ParameterObject Credentials credentials,
        @RequestBody CreateWatchlistRequest request
    ) {
        createWatchlistUseCase.execute(new CreateWatchlistCommand(
            new AccountId(credentials.accountId()),
            new Username(credentials.username()),
            new Password(credentials.password()),
            new Title(request.title()),
            new LinkedHashSet<>(request.itemsAsBookshelfEntryIds().stream().map(BookshelfEntryId::new).toList())
        ));
    }

    @PatchMapping("/{watchlistId}")
    public void updateWatchlist(
        @PathVariable UUID watchlistId,
        @ParameterObject Credentials credentials,
        @RequestBody UpdateWatchlistRequest request
    ) {
        updateWatchlistUseCase.execute(new UpdateWatchlistCommand(
            new AccountId(credentials.accountId()),
            new Username(credentials.username()),
            new Password(credentials.password()),
            new WatchlistId(watchlistId),
            Optional.ofNullable(request.newTitle()).map(Title::new),
            Optional.ofNullable(request.newItemsAsBookshelfEntryIds())
                    .map(items -> new LinkedHashSet<>(items.stream().map(BookshelfEntryId::new).toList())),
            Optional.ofNullable(request.removeItemsAsBookshelfEntryIds())
                    .map(items -> items.stream().map(BookshelfEntryId::new).collect(Collectors.toSet()))
        ));
    }

    @DeleteMapping("/{watchlistId}")
    public void deleteWatchlist(@PathVariable UUID watchlistId, @ParameterObject Credentials credentials) {
        deleteWatchlistUseCase.execute(new DeleteWatchlistCommand(
            new AccountId(credentials.accountId()),
            new Username(credentials.username()),
            new Password(credentials.password()),
            new WatchlistId(watchlistId)
        ));
    }

    @GetMapping
    public ResponseEntity<GetAllWatchlistsResult> getAllWatchlists(@ParameterObject Credentials credentials) {
        return ResponseEntity.ok(getAllWatchlistsUseCase.execute(new GetAllWatchlistsCommand(
            new AccountId(credentials.accountId()),
            new Username(credentials.username()),
            new Password(credentials.password())
        )));
    }

    @GetMapping("/{watchlistId}")
    public ResponseEntity<GetWatchlistByIdResult> getWatchlistById(
        @PathVariable UUID watchlistId, @ParameterObject Credentials credentials
    ) {
        return ResponseEntity.of(getWatchlistByIdUseCase.execute(new GetWatchlistByIdCommand(
            new AccountId(credentials.accountId()),
            new Username(credentials.username()),
            new Password(credentials.password()),
            new WatchlistId(watchlistId)
        )));
    }

    /**
     * Removes all items of provided consumption states.
     */
    @PatchMapping("/{watchlistId}/remove-items-by-consumption")
    public void removeWatchlistItemsByConsumptionState(
        @PathVariable UUID watchlistId,
        @ParameterObject Credentials credentials,
        @RequestBody RemoveWatchlistItemsByConsumptionStateRequest request
    ) {
        removeWatchlistItemsByConsumptionStateUseCase.execute(
            new RemoveWatchlistItemsByConsumptionStateCommand(
                new AccountId(credentials.accountId()),
                new Username(credentials.username()),
                new Password(credentials.password()),
                new WatchlistId(watchlistId),
                request.consumptionStates()
            )
        );
    }

}
