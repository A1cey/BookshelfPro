package org.a1cey.bookshelf_pro_application.media_item.review;

import org.a1cey.bookshelf_pro_application.media_item.review.command.UpdateReviewCommand;
import org.a1cey.bookshelf_pro_application.security.CurrentUserProvider;
import org.a1cey.bookshelf_pro_domain.bookshelf.bookshelf_entry.BookshelfEntryRepository;
import org.a1cey.bookshelf_pro_domain.media_item.review.ReviewRepository;
import org.springframework.stereotype.Service;

@Service
public class UpdateReviewUseCase {
    private final ReviewRepository reviewRepository;
    private final BookshelfEntryRepository bookshelfEntryRepository;
    private final CurrentUserProvider currentUserProvider;

    public UpdateReviewUseCase(ReviewRepository reviewRepository, BookshelfEntryRepository bookshelfEntryRepository,
                               CurrentUserProvider currentUserProvider) {
        this.reviewRepository = reviewRepository;
        this.bookshelfEntryRepository = bookshelfEntryRepository;
        this.currentUserProvider = currentUserProvider;
    }

    public void execute(UpdateReviewCommand command) {
        if (command.newComment().isEmpty() && command.newRating().isEmpty()) {
            return;
        }

        var owner = currentUserProvider.currentUser();

        var review = reviewRepository
                         .findById(command.reviewId())
                         .orElseThrow(() -> new IllegalArgumentException("Review with id " + command.reviewId() + " not found"));

        if (!review.owner().equals(owner.id())) {
            throw new SecurityException("User not allowed to perform this action as they do not own the review");
        }

        var bookshelfEntry = bookshelfEntryRepository
                                 .findByAccountAndMediaItem(owner.id(), review.mediaItemId())
                                 .orElseThrow(() -> new IllegalStateException(
                                     "Bookshelf entry with id " + review.mediaItemId() + " not found even though a review exists"
                                 ));

        if (command.newComment().isEmpty()) {
            review.changeRating(
                command.newRating().get(),
                bookshelfEntry.consumptionProgress().snapshot(),
                owner.id()
            );
        } else if (command.newRating().isEmpty()) {
            review.changeComment(
                command.newComment().get(),
                bookshelfEntry.consumptionProgress().snapshot(),
                owner.id()
            );
        } else {
            review.changeReview(
                command.newRating().get(),
                command.newComment().get(),
                bookshelfEntry.consumptionProgress().snapshot(),
                owner.id()
            );
        }

        reviewRepository.update(review, bookshelfEntry);
    }
}
