package org.a1cey.bookshelf_pro_plugins.rest.bookshelf.bookshelf_entry;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.a1cey.bookshelf_pro_application.bookshelf.bookshelf_entry.AddBookshelfEntryUseCase;
import org.a1cey.bookshelf_pro_application.bookshelf.bookshelf_entry.GetAllBookshelfEntriesUseCase;
import org.a1cey.bookshelf_pro_application.bookshelf.bookshelf_entry.GetBookshelfEntryUseCase;
import org.a1cey.bookshelf_pro_application.bookshelf.bookshelf_entry.UpdateBookshelfEntryUseCase;
import org.a1cey.bookshelf_pro_application.bookshelf.bookshelf_entry.command.AddBookshelfEntryCommand;
import org.a1cey.bookshelf_pro_application.bookshelf.bookshelf_entry.command.GetBookshelfEntryCommand;
import org.a1cey.bookshelf_pro_application.bookshelf.bookshelf_entry.command.UpdateBookshelfEntryCommand;
import org.a1cey.bookshelf_pro_application.bookshelf.bookshelf_entry.result.AddBookshelfEntryResult;
import org.a1cey.bookshelf_pro_application.bookshelf.bookshelf_entry.result.GetAllBookshelfEntriesResult;
import org.a1cey.bookshelf_pro_application.bookshelf.bookshelf_entry.result.GetBookshelfEntryResult;
import org.a1cey.bookshelf_pro_domain.bookshelf.bookshelf_entry.BookshelfEntryId;
import org.a1cey.bookshelf_pro_domain.bookshelf.bookshelf_entry.Label;
import org.a1cey.bookshelf_pro_domain.media_item.MediaItemId;
import org.a1cey.bookshelf_pro_plugins.rest.bookshelf.bookshelf_entry.request.AddBookshelfEntryRequest;
import org.a1cey.bookshelf_pro_plugins.rest.bookshelf.bookshelf_entry.request.UpdateBookshelfEntryRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bookshelf/entry")
public class BookshelfEntryController {
    private final AddBookshelfEntryUseCase addBookshelfEntryUseCase;
    private final GetBookshelfEntryUseCase getBookshelfEntryUseCase;
    private final UpdateBookshelfEntryUseCase updateBookshelfEntryUseCase;
    private final GetAllBookshelfEntriesUseCase getAllBookshelfEntriesUseCase;

    public BookshelfEntryController(
        AddBookshelfEntryUseCase addBookshelfEntryUseCase,
        GetBookshelfEntryUseCase getBookshelfEntryUseCase,
        UpdateBookshelfEntryUseCase updateBookshelfEntryUseCase,
        GetAllBookshelfEntriesUseCase getAllBookshelfEntriesUseCase) {
        this.addBookshelfEntryUseCase = addBookshelfEntryUseCase;
        this.getBookshelfEntryUseCase = getBookshelfEntryUseCase;
        this.updateBookshelfEntryUseCase = updateBookshelfEntryUseCase;
        this.getAllBookshelfEntriesUseCase = getAllBookshelfEntriesUseCase;
    }

    @PostMapping
    public ResponseEntity<AddBookshelfEntryResult> addBookshelfEntry(@RequestBody AddBookshelfEntryRequest request) {
        return ResponseEntity.ok(addBookshelfEntryUseCase.execute(
            new AddBookshelfEntryCommand(
                new MediaItemId(request.mediaItemId()),
                request.labels().stream().map(Label::new).collect(Collectors.toSet())
            )
        ));
    }

    @GetMapping("/{bookshelfEntryId}")
    public ResponseEntity<GetBookshelfEntryResult> getBookshelfEntry(@PathVariable UUID bookshelfEntryId) {
        var entry = getBookshelfEntryUseCase.execute(
            new GetBookshelfEntryCommand(new BookshelfEntryId(bookshelfEntryId))
        );

        return ResponseEntity.of(entry);
    }

    @PatchMapping("/{bookshelfEntryId}")
    public void updateBookshelfEntry(@PathVariable UUID bookshelfEntryId, @RequestBody UpdateBookshelfEntryRequest request) {
        var command = new UpdateBookshelfEntryCommand(
            new BookshelfEntryId(bookshelfEntryId),
            Optional.ofNullable(request.consumptionProgressNumber()),
            Optional.ofNullable(request.labels()).map(labels -> labels.stream().map(Label::new).collect(Collectors.toSet()))
        );

        updateBookshelfEntryUseCase.execute(command);
    }

    @GetMapping
    public ResponseEntity<GetAllBookshelfEntriesResult> getAllBookshelfEntries() {
        var entries = getAllBookshelfEntriesUseCase.execute();

        return ResponseEntity.ok(entries);
    }
}
