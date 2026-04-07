package org.a1cey.bookshelf_pro_application.media_item.review;

import org.a1cey.bookshelf_pro_application.SecurityService;
import org.a1cey.bookshelf_pro_application.bookshelf.bookshelf_entry.AddBookshelfEntryUseCase;
import org.a1cey.bookshelf_pro_application.media_item.review.command.UpdateReviewCommand;
import org.a1cey.bookshelf_pro_domain.bookshelf.bookshelf_entry.BookshelfEntryRepository;
import org.a1cey.bookshelf_pro_domain.media_item.review.ReviewRepository;
import org.springframework.stereotype.Service;

@Service
public class UpdateReviewUseCase {
    private final ReviewRepository reviewRepository;
    private final SecurityService securityService;
    private final BookshelfEntryRepository bookshelfEntryRepository;
    private final AddBookshelfEntryUseCase addBookshelfEntryUseCase;

    public UpdateReviewUseCase(
        ReviewRepository reviewRepository,
        SecurityService securityService,
        BookshelfEntryRepository bookshelfEntryRepository,
        AddBookshelfEntryUseCase addBookshelfEntryUseCase
    ) {
        this.reviewRepository = reviewRepository;
        this.securityService = securityService;
        this.bookshelfEntryRepository = bookshelfEntryRepository;
        this.addBookshelfEntryUseCase = addBookshelfEntryUseCase;
    }

    public void execute(UpdateReviewCommand command) {
        if (command.newComment().isEmpty() && command.newRating().isEmpty()) {
            return;
        }

        var account = securityService.checkUser(command.accountId(), command.name(), command.password());

        var review = reviewRepository
                         .findById(command.reviewId())
                         .orElseThrow(() -> new IllegalArgumentException("Review with id " + command.reviewId() + " not found"));

        if (!review.owner().equals(account.id())) {
            throw new SecurityException("User not allowed to perform this action as they do not own the review");
        }

        var bookshelfEntry = bookshelfEntryRepository
                                 .findByAccountAndMediaItem(account.id(), review.mediaItemId())
                                 .orElseThrow(() -> new IllegalStateException(
                                     "Bookshelf entry with id " + review.mediaItemId() + " not found even though a review exists"
                                 ));

        if (command.newComment().isEmpty()) {
            review.changeRating(
                command.newRating().get(),
                bookshelfEntry.consumptionProgress().snapshot(),
                account.id()
            );
        } else if (command.newRating().isEmpty()) {
            review.changeComment(
                command.newComment().get(),
                bookshelfEntry.consumptionProgress().snapshot(),
                account.id()
            );
        } else {
            review.changeReview(
                command.newRating().get(),
                command.newComment().get(),
                bookshelfEntry.consumptionProgress().snapshot(),
                account.id()
            );
        }

        reviewRepository.update(review, bookshelfEntry);
    }
}
