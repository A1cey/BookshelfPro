package org.a1cey.bookshelf_pro_application.media_item.review;

import java.util.Optional;

import org.a1cey.bookshelf_pro_application.dto.ReviewDto;
import org.a1cey.bookshelf_pro_application.media_item.review.command.GetReviewCommand;
import org.a1cey.bookshelf_pro_application.media_item.review.result.GetReviewResult;
import org.a1cey.bookshelf_pro_domain.media_item.review.ReviewRepository;
import org.springframework.stereotype.Service;

@Service
public class GetReviewUseCase {

    private final ReviewRepository reviewRepository;

    public GetReviewUseCase(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public Optional<GetReviewResult> execute(GetReviewCommand command) {
        return reviewRepository.findById(command.reviewId()).map(review -> new GetReviewResult(ReviewDto.from(review)));
    }
}
