package org.a1cey.bookshelf_pro_plugins.rest.bookshelf.bookshelf_entry;

import java.util.UUID;
import java.util.stream.Collectors;

import org.a1cey.bookshelf_pro_application.bookshelf.bookshelf_entry.AddBookshelfEntryUseCase;
import org.a1cey.bookshelf_pro_application.bookshelf.bookshelf_entry.GetBookshelfEntryUseCase;
import org.a1cey.bookshelf_pro_application.bookshelf.bookshelf_entry.command.AddBookshelfEntryCommand;
import org.a1cey.bookshelf_pro_application.bookshelf.bookshelf_entry.command.GetBookshelfEntryCommand;
import org.a1cey.bookshelf_pro_application.bookshelf.bookshelf_entry.result.GetBookshelfEntryResult;
import org.a1cey.bookshelf_pro_domain.account.AccountId;
import org.a1cey.bookshelf_pro_domain.account.Password;
import org.a1cey.bookshelf_pro_domain.account.Username;
import org.a1cey.bookshelf_pro_domain.bookshelf.bookshelf_entry.BookshelfEntryId;
import org.a1cey.bookshelf_pro_domain.bookshelf.bookshelf_entry.Label;
import org.a1cey.bookshelf_pro_domain.media_item.MediaItemId;
import org.a1cey.bookshelf_pro_plugins.rest.bookshelf.bookshelf_entry.request.AddBookshelfEntryRequest;
import org.a1cey.bookshelf_pro_plugins.rest.bookshelf.bookshelf_entry.request.GetBookshelfEntryRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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

    public BookshelfEntryController(AddBookshelfEntryUseCase addBookshelfEntryUseCase, GetBookshelfEntryUseCase getBookshelfEntryUseCase) {
        this.addBookshelfEntryUseCase = addBookshelfEntryUseCase;
        this.getBookshelfEntryUseCase = getBookshelfEntryUseCase;
    }

    @PostMapping
    public void addBookshelfEntry(@RequestBody AddBookshelfEntryRequest request) {
        addBookshelfEntryUseCase.execute(
            new AddBookshelfEntryCommand(
                new AccountId(request.accountId()),
                new Username(request.name()),
                new Password(request.password()),
                new MediaItemId(request.mediaItemId()),
                request.labels().stream().map(Label::new).collect(Collectors.toSet())
            )
        );
    }

    @GetMapping("/{bookshelfEntryId}")
    public ResponseEntity<GetBookshelfEntryResult> getBookshelfEntry(
        @PathVariable UUID bookshelfEntryId,
        @RequestBody GetBookshelfEntryRequest request
    ) {
        var entry = getBookshelfEntryUseCase.execute(
            new GetBookshelfEntryCommand(
                new AccountId(request.accountId()),
                new Username(request.name()),
                new Password(request.password()),
                new BookshelfEntryId(bookshelfEntryId)
            )
        );

        return ResponseEntity.of(entry);
    }
}
