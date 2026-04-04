package org.a1cey.bookshelf_pro_application.media_item.review;

import java.util.Optional;

import org.a1cey.bookshelf_pro_application.dto.ReviewDto;
import org.a1cey.bookshelf_pro_application.media_item.review.command.GetReviewByIdCommand;
import org.a1cey.bookshelf_pro_application.media_item.review.result.GetReviewByIdResult;
import org.a1cey.bookshelf_pro_domain.media_item.review.ReviewRepository;
import org.springframework.stereotype.Service;

@Service
public class GetReviewByIdUseCase {

    private final ReviewRepository reviewRepository;

    public GetReviewByIdUseCase(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public Optional<GetReviewByIdResult> execute(GetReviewByIdCommand command) {
        return reviewRepository.findById(command.reviewId()).map(review -> new GetReviewByIdResult(ReviewDto.from(review)));
    }
}
