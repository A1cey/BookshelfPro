package org.a1cey.bookshelf_pro_application.media_item.review;

import org.a1cey.bookshelf_pro_application.SecurityService;
import org.a1cey.bookshelf_pro_application.media_item.review.command.DeleteReviewCommand;
import org.a1cey.bookshelf_pro_domain.media_item.review.ReviewRepository;
import org.springframework.stereotype.Service;

@Service
public class DeleteReviewUseCase {

    private final SecurityService securityService;
    private final ReviewRepository reviewRepository;

    public DeleteReviewUseCase(SecurityService securityService, ReviewRepository reviewRepository) {
        this.securityService = securityService;
        this.reviewRepository = reviewRepository;
    }

    public void execute(DeleteReviewCommand command) {
        var account = securityService.checkUser(command.owner(), command.username(), command.password());
        reviewRepository.delete(command.reviewId(), account.id());
    }
}
