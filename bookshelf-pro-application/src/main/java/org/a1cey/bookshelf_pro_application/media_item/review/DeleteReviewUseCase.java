package org.a1cey.bookshelf_pro_application.media_item.review;

import org.a1cey.bookshelf_pro_application.SecurityService;
import org.a1cey.bookshelf_pro_application.media_item.review.command.DeleteReviewCommand;
import org.a1cey.bookshelf_pro_domain.media_item.review.ReviewRepository;
import org.springframework.stereotype.Service;

@Service
public class DeleteReviewUseCase {

    private final ReviewRepository reviewRepository;
    private final SecurityService securityService;

    public DeleteReviewUseCase(ReviewRepository reviewRepository, SecurityService securityService) {
        this.reviewRepository = reviewRepository;
        this.securityService = securityService;
    }

    public void execute(DeleteReviewCommand command) {
        securityService.checkUser(command.owner(), command.name(), command.password());

        reviewRepository.findById(command.reviewId()).ifPresent(review -> {
            if (!review.owner().equals(command.owner())) {
                throw new SecurityException("User not allowed to perform this action as they do not own the review");
            }
        });

        reviewRepository.delete(command.reviewId());
    }
}
