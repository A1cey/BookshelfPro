package org.a1cey.bookshelf_pro_plugins.rest.media_item.book;

import java.util.Optional;
import java.util.UUID;

import org.a1cey.bookshelf_pro_application.media_item.book.CreateBookUseCase;
import org.a1cey.bookshelf_pro_application.media_item.book.command.CreateBookCommand;
import org.a1cey.bookshelf_pro_application.media_item.book.result.CreateBookResult;
import org.a1cey.bookshelf_pro_domain.Title;
import org.a1cey.bookshelf_pro_domain.account.AccountId;
import org.a1cey.bookshelf_pro_domain.media_item.Description;
import org.a1cey.bookshelf_pro_domain.media_item.Language;
import org.a1cey.bookshelf_pro_domain.media_item.Subtitle;
import org.a1cey.bookshelf_pro_domain.media_item.book.Author;
import org.a1cey.bookshelf_pro_domain.media_item.book.Isbn;
import org.a1cey.bookshelf_pro_domain.media_item.book.PageCount;
import org.a1cey.bookshelf_pro_domain.media_item.book.PublishDate;
import org.a1cey.bookshelf_pro_domain.media_item.book.PublishPlace;
import org.a1cey.bookshelf_pro_domain.media_item.book.Publisher;
import org.a1cey.bookshelf_pro_plugins.rest.media_item.book.request.CreateBookRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/media-item/book")
public class BookController {

    private final CreateBookUseCase createBookUseCase;

    public BookController(CreateBookUseCase createBookUseCase) {
        this.createBookUseCase = createBookUseCase;
    }

    @PostMapping
    public ResponseEntity<CreateBookResult> createBook(@RequestBody CreateBookRequest request) {
        // TODO: replace with real auth
        var requestingUser = new AccountId(UUID.randomUUID());

        var command = new CreateBookCommand(
            requestingUser,
            new Title(request.title()),
            new Isbn(request.isbn()),
            new PageCount(request.pageCount()),
            Optional.ofNullable(request.subtitle()).map(Subtitle::new),
            Optional.ofNullable(request.description()).map(Description::new),
            Optional.ofNullable(request.coverImageUrl()),
            Optional.ofNullable(request.authors()).map(a -> a.stream().map(Author::new).toList()),
            Optional.ofNullable(request.publishDate()).map(PublishDate::new),
            Optional.ofNullable(request.publisher()).map(Publisher::new),
            Optional.ofNullable(request.publishPlace()).map(PublishPlace::new),
            Optional.ofNullable(request.languages()).map(ls -> ls.stream().map(Language::new).collect(java.util.stream.Collectors.toSet()))
        );

        return ResponseEntity.ok(createBookUseCase.execute(command));
    }
}