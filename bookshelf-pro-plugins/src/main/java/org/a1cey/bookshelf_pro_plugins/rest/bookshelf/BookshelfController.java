package org.a1cey.bookshelf_pro_plugins.rest.bookshelf;

import org.a1cey.bookshelf_pro_application.bookshelf.GetAllBookshelfEntriesUseCase;
import org.a1cey.bookshelf_pro_application.bookshelf.command.GetAllBookshelfEntriesCommand;
import org.a1cey.bookshelf_pro_application.bookshelf.result.GetAllBookshelfEntriesResult;
import org.a1cey.bookshelf_pro_domain.account.AccountId;
import org.a1cey.bookshelf_pro_domain.account.Password;
import org.a1cey.bookshelf_pro_domain.account.Username;
import org.a1cey.bookshelf_pro_plugins.rest.bookshelf.request.GetAllBookshelfEntriesRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bookshelf")
public class BookshelfController {
    private final GetAllBookshelfEntriesUseCase getAllBookshelfEntriesUseCase;

    public BookshelfController(GetAllBookshelfEntriesUseCase getAllBookshelfEntriesUseCase) {
        this.getAllBookshelfEntriesUseCase = getAllBookshelfEntriesUseCase;
    }

    @GetMapping("entry")
    public ResponseEntity<GetAllBookshelfEntriesResult> getAllBookshelfEntries(@RequestBody GetAllBookshelfEntriesRequest request) {
        var entries = getAllBookshelfEntriesUseCase.execute(
            new GetAllBookshelfEntriesCommand(
                new AccountId(request.accountId()),
                new Username(request.name()),
                new Password(request.password())
            )
        );

        return ResponseEntity.ok(entries);
    }
}
