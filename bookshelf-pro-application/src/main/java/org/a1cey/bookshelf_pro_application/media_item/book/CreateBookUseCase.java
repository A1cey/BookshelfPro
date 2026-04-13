package org.a1cey.bookshelf_pro_application.media_item.book;

import org.a1cey.bookshelf_pro_application.IdService;
import org.a1cey.bookshelf_pro_application.media_item.book.command.CreateBookCommand;
import org.a1cey.bookshelf_pro_application.media_item.book.result.CreateBookResult;
import org.a1cey.bookshelf_pro_application.security.CurrentUserProvider;
import org.a1cey.bookshelf_pro_domain.media_item.MediaItemId;
import org.a1cey.bookshelf_pro_domain.media_item.MediaItemRepository;
import org.a1cey.bookshelf_pro_domain.media_item.book.Book;
import org.springframework.stereotype.Service;

@Service
public final class CreateBookUseCase {
    private final MediaItemRepository mediaItemRepository;
    private final IdService idService;
    private final CurrentUserProvider currentUserProvider;

    public CreateBookUseCase(MediaItemRepository mediaItemRepository, IdService idService, CurrentUserProvider currentUserProvider) {
        this.mediaItemRepository = mediaItemRepository;
        this.idService = idService;
        this.currentUserProvider = currentUserProvider;
    }

    public CreateBookResult execute(CreateBookCommand command) {
        var owner = currentUserProvider.currentUser();
        var id = new MediaItemId(idService.generateId());

        var builder = Book.builder(id, owner.id(), command.title(), command.isbn(), command.pageCount());

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
