package org.a1cey.bookshelf_pro_plugins.rest.media_item.book;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.a1cey.bookshelf_pro_application.media_item.book.CreateBookUseCase;
import org.a1cey.bookshelf_pro_application.media_item.book.GetAllBooksUseCase;
import org.a1cey.bookshelf_pro_application.media_item.book.GetBookByIdUseCase;
import org.a1cey.bookshelf_pro_application.media_item.book.UpdateBookUseCase;
import org.a1cey.bookshelf_pro_application.media_item.book.command.CreateBookCommand;
import org.a1cey.bookshelf_pro_application.media_item.book.command.GetBookCommand;
import org.a1cey.bookshelf_pro_application.media_item.book.command.UpdateBookCommand;
import org.a1cey.bookshelf_pro_application.media_item.book.result.CreateBookResult;
import org.a1cey.bookshelf_pro_application.media_item.book.result.GetAllBooksResult;
import org.a1cey.bookshelf_pro_application.media_item.book.result.GetBookByIdResult;
import org.a1cey.bookshelf_pro_domain.Title;
import org.a1cey.bookshelf_pro_domain.account.AccountId;
import org.a1cey.bookshelf_pro_domain.media_item.Description;
import org.a1cey.bookshelf_pro_domain.media_item.Language;
import org.a1cey.bookshelf_pro_domain.media_item.MediaItemId;
import org.a1cey.bookshelf_pro_domain.media_item.Subtitle;
import org.a1cey.bookshelf_pro_domain.media_item.book.Author;
import org.a1cey.bookshelf_pro_domain.media_item.book.Isbn;
import org.a1cey.bookshelf_pro_domain.media_item.book.PageCount;
import org.a1cey.bookshelf_pro_domain.media_item.book.PublishDate;
import org.a1cey.bookshelf_pro_domain.media_item.book.PublishPlace;
import org.a1cey.bookshelf_pro_domain.media_item.book.Publisher;
import org.a1cey.bookshelf_pro_plugins.rest.media_item.book.request.CreateBookRequest;
import org.a1cey.bookshelf_pro_plugins.rest.media_item.book.request.UpdateBookRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/media-item/book")
public class BookController {

    private final CreateBookUseCase createBookUseCase;
    private final UpdateBookUseCase updateBookUseCase;
    private final GetBookByIdUseCase getBookByIdUseCase;
    private final GetAllBooksUseCase getAllBooksUseCase;

    public BookController(CreateBookUseCase createBookUseCase, UpdateBookUseCase updateBookUseCase, GetBookByIdUseCase getBookByIdUseCase
        , GetAllBooksUseCase getAllBooksUseCase) {
        this.createBookUseCase = createBookUseCase;
        this.updateBookUseCase = updateBookUseCase;
        this.getBookByIdUseCase = getBookByIdUseCase;
        this.getAllBooksUseCase = getAllBooksUseCase;
    }

    @PostMapping
    public ResponseEntity<CreateBookResult> createBook(@RequestBody CreateBookRequest request) {
        var command = new CreateBookCommand(
            new AccountId(request.requestingAccountId()),
            new Title(request.title()),
            Optional.ofNullable(request.subtitle()).map(Subtitle::new),
            Optional.ofNullable(request.description()).map(Description::new),
            Optional.ofNullable(request.coverImageUrl()),
            Optional.ofNullable(request.languages()).map(ls -> ls.stream().map(Language::new).collect(Collectors.toSet())),
            new Isbn(request.isbn()),
            new PageCount(request.pageCount()),
            Optional.ofNullable(request.authors()).map(a -> a.stream().map(Author::new).collect(Collectors.toSet())),
            Optional.ofNullable(request.publishDate()).map(PublishDate::new),
            Optional.ofNullable(request.publisher()).map(Publisher::new),
            Optional.ofNullable(request.publishPlace()).map(PublishPlace::new)
        );

        return ResponseEntity.ok(createBookUseCase.execute(command));
    }

    @PatchMapping("/{id}")
    public void updateBook(@PathVariable UUID id, @RequestBody UpdateBookRequest request) {
        var command = new UpdateBookCommand(
            new AccountId(request.requestingAccountId()),
            new MediaItemId(id),
            Optional.ofNullable(request.title()).map(Title::new),
            Optional.ofNullable(request.subtitle()).map(Subtitle::new),
            Optional.ofNullable(request.description()).map(Description::new),
            Optional.ofNullable(request.coverImageUrl()),
            Optional.ofNullable(request.languages()).map(ls -> ls.stream().map(Language::new).collect(Collectors.toSet())),
            Optional.ofNullable(request.pageCount()).map(PageCount::new),
            Optional.ofNullable(request.authors()).map(a -> a.stream().map(Author::new).collect(Collectors.toSet())),
            Optional.ofNullable(request.publishDate()).map(PublishDate::new),
            Optional.ofNullable(request.publisher()).map(Publisher::new),
            Optional.ofNullable(request.publishPlace()).map(PublishPlace::new)
        );

        updateBookUseCase.execute(command);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetBookByIdResult> getBookById(@PathVariable UUID id) {
        var command = new GetBookCommand(new MediaItemId(id));
        return getBookByIdUseCase
                   .execute(command)
                   .map(ResponseEntity::ok)
                   .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping()
    public ResponseEntity<GetAllBooksResult> getAllBooks() {
        return ResponseEntity.ok(getAllBooksUseCase.execute());
    }
}