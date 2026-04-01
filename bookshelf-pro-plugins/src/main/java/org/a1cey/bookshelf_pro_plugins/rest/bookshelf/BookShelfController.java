package org.a1cey.bookshelf_pro_plugins.rest.bookshelf;

import java.util.stream.Collectors;

import org.a1cey.bookshelf_pro_application.bookshelf_entry.AddBookshelfEntryUseCase;
import org.a1cey.bookshelf_pro_application.bookshelf_entry.command.AddBookshelfEntryCommand;
import org.a1cey.bookshelf_pro_domain.account.AccountId;
import org.a1cey.bookshelf_pro_domain.account.Password;
import org.a1cey.bookshelf_pro_domain.account.Username;
import org.a1cey.bookshelf_pro_domain.bookshelf_entry.Label;
import org.a1cey.bookshelf_pro_domain.media_item.MediaItemId;
import org.a1cey.bookshelf_pro_plugins.rest.bookshelf.request.AddBookshelfEntryRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bookshelf")
public class BookShelfController {
    private final AddBookshelfEntryUseCase addBookshelfEntryUseCase;

    public BookShelfController(AddBookshelfEntryUseCase addBookshelfEntryUseCase) {
        this.addBookshelfEntryUseCase = addBookshelfEntryUseCase;
    }

    @PostMapping
    public void addBookshelfEntry(@RequestBody AddBookshelfEntryRequest request) {
        System.out.println(request.labels());

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
}
