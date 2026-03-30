package org.a1cey.bookshelf_pro_application.media_item.book;

import java.util.UUID;

import org.a1cey.bookshelf_pro_application.media_item.book.command.CreateBookCommand;
import org.a1cey.bookshelf_pro_application.media_item.book.result.CreateBookResult;
import org.a1cey.bookshelf_pro_domain.account.AccountRepository;
import org.a1cey.bookshelf_pro_domain.media_item.MediaItemId;
import org.a1cey.bookshelf_pro_domain.media_item.MediaItemRepository;
import org.a1cey.bookshelf_pro_domain.media_item.book.Book;
import org.springframework.stereotype.Service;

@Service
public class CreateBookUseCase {
    private final AccountRepository accountRepository;
    private final MediaItemRepository mediaItemRepository;

    public CreateBookUseCase(AccountRepository accountRepository, MediaItemRepository mediaItemRepository) {
        this.accountRepository = accountRepository;
        this.mediaItemRepository = mediaItemRepository;
    }

    public CreateBookResult execute(CreateBookCommand command) {
        accountRepository.findById(command.requestingUser()).orElseThrow(() -> new IllegalArgumentException(
            "Account not found: " + command.requestingUser().value()
        ));

        var id = new MediaItemId(UUID.randomUUID()); // This is enough, generating the same UUID4 twice is extremely unlikely (no problem)

        var builder = Book.builder(id, command.requestingUser(), command.title(), command.isbn(), command.pageCount());

        command.authors().ifPresent(builder::authors);
        command.publisher().ifPresent(builder::publisher);
        command.publishDate().ifPresent(builder::publishDate);
        command.publishPlace().ifPresent(builder::publishPlace);
        command.description().ifPresent(builder::description);
        command.subtitle().ifPresent(builder::subtitle);
        command.languages().ifPresent(builder::languages);
        command.coverImageUrl().ifPresent(builder::coverImageUrl);

        var book = builder.build();

        mediaItemRepository.save(book);

        return new CreateBookResult(id);
    }
}
