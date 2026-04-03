package org.a1cey.bookshelf_pro_application.media_item.book;

import java.util.NoSuchElementException;

import org.a1cey.bookshelf_pro_application.SecurityService;
import org.a1cey.bookshelf_pro_application.media_item.book.command.UpdateBookCommand;
import org.a1cey.bookshelf_pro_domain.media_item.MediaItemRepository;
import org.a1cey.bookshelf_pro_domain.media_item.book.Book;
import org.springframework.stereotype.Service;

@Service
public final class UpdateBookUseCase {

    private final MediaItemRepository mediaItemRepository;
    private final SecurityService securityService;

    public UpdateBookUseCase(MediaItemRepository mediaItemRepository, SecurityService securityService) {
        this.mediaItemRepository = mediaItemRepository;
        this.securityService = securityService;
    }

    public void execute(UpdateBookCommand command) {
        var account = securityService.checkUser(command.accountId(), command.name(), command.password());

        var mediaItem = mediaItemRepository
                            .findById(command.bookId())
                            .orElseThrow(() -> new NoSuchElementException("Book with id " + command.bookId() + " not found"));

        if (!mediaItem.owner().equals(account.id())) {
            throw new SecurityException("User not allowed to perform this action as they do not own the media item");
        }

        if (!(mediaItem instanceof Book book)) {
            throw new IllegalArgumentException("Book id does not match a book but another media item type: " + mediaItem.type());
        }

        command.title().ifPresent(newTitle -> book.changeTitle(newTitle, account.id()));
        command.subtitle().ifPresent(subtitle -> book.changeSubtitle(subtitle, account.id()));
        command.description().ifPresent(newDescription -> book.changeDescription(newDescription, account.id()));
        command.coverImageUrl().ifPresent(newCoverImageUrl -> book.changeCoverImageUrl(newCoverImageUrl, account.id()));
        command.languages().ifPresent(newLanguages -> book.changeLanguages(newLanguages, account.id()));
        command.pageCount().ifPresent(newPageCount -> book.changePageCount(newPageCount, account.id()));
        command.publisher().ifPresent(newPublisher -> book.changePublisher(newPublisher, account.id()));
        command.publishDate().ifPresent(newPublishDate -> book.changePublishDate(newPublishDate, account.id()));
        command.publishPlace().ifPresent(newPublishPlace -> book.changePublishPlace(newPublishPlace, account.id()));
        command.authors().ifPresent(newAuthors -> book.changeAuthors(newAuthors, account.id()));

        mediaItemRepository.update(book);
    }
}
