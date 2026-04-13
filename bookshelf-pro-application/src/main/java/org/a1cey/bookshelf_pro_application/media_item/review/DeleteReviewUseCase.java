package org.a1cey.bookshelf_pro_application.media_item.review;

import org.a1cey.bookshelf_pro_application.media_item.review.command.DeleteReviewCommand;
import org.a1cey.bookshelf_pro_application.security.CurrentUserProvider;
import org.a1cey.bookshelf_pro_domain.media_item.review.ReviewRepository;
import org.springframework.stereotype.Service;

@Service
public class DeleteReviewUseCase {
    private final ReviewRepository reviewRepository;
    private final CurrentUserProvider currentUserProvider;

    public DeleteReviewUseCase(ReviewRepository reviewRepository, CurrentUserProvider currentUserProvider) {
        this.reviewRepository = reviewRepository;
        this.currentUserProvider = currentUserProvider;
    }

    public void execute(DeleteReviewCommand command) {
        var owner = currentUserProvider.currentUser();

        reviewRepository.findById(command.reviewId()).ifPresent(review -> {
            if (!review.owner().equals(owner.id())) {
                throw new SecurityException("User not allowed to perform this action as they do not own the review");
            }
        });

        reviewRepository.delete(command.reviewId());
    }
}
