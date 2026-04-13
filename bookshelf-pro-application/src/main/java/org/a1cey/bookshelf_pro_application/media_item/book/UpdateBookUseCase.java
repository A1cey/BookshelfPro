package org.a1cey.bookshelf_pro_application.media_item.book;

import java.util.NoSuchElementException;

import org.a1cey.bookshelf_pro_application.media_item.book.command.UpdateBookCommand;
import org.a1cey.bookshelf_pro_application.security.CurrentUserProvider;
import org.a1cey.bookshelf_pro_domain.media_item.MediaItemRepository;
import org.a1cey.bookshelf_pro_domain.media_item.book.Book;
import org.springframework.stereotype.Service;

@Service
public final class UpdateBookUseCase {

    private final MediaItemRepository mediaItemRepository;
    private final CurrentUserProvider currentUserProvider;

    public UpdateBookUseCase(MediaItemRepository mediaItemRepository, CurrentUserProvider currentUserProvider) {
        this.mediaItemRepository = mediaItemRepository;
        this.currentUserProvider = currentUserProvider;
    }

    public void execute(UpdateBookCommand command) {
        var owner = currentUserProvider.currentUser();
        var mediaItem = mediaItemRepository
                            .findById(command.bookId())
                            .orElseThrow(() -> new NoSuchElementException("Book with id " + command.bookId() + " not found"));

        if (!mediaItem.owner().equals(owner.id())) {
            throw new SecurityException("User not allowed to perform this action as they do not own the media item");
        }

        if (!(mediaItem instanceof Book book)) {
            throw new IllegalArgumentException("Book id does not match a book but another media item type: " + mediaItem.type());
        }

        command.title().ifPresent(newTitle -> book.changeTitle(newTitle, owner.id()));
        command.subtitle().ifPresent(subtitle -> book.changeSubtitle(subtitle, owner.id()));
        command.description().ifPresent(newDescription -> book.changeDescription(newDescription, owner.id()));
        command.coverImageUrl().ifPresent(newCoverImageUrl -> book.changeCoverImageUrl(newCoverImageUrl, owner.id()));
        command.languages().ifPresent(newLanguages -> book.changeLanguages(newLanguages, owner.id()));
        command.pageCount().ifPresent(newPageCount -> book.changePageCount(newPageCount, owner.id()));
        command.publisher().ifPresent(newPublisher -> book.changePublisher(newPublisher, owner.id()));
        command.publishDate().ifPresent(newPublishDate -> book.changePublishDate(newPublishDate, owner.id()));
        command.publishPlace().ifPresent(newPublishPlace -> book.changePublishPlace(newPublishPlace, owner.id()));
        command.authors().ifPresent(newAuthors -> book.changeAuthors(newAuthors, owner.id()));

        mediaItemRepository.update(book);
    }
}
