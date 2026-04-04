package org.a1cey.bookshelf_pro_application.media_item.review;

import java.util.stream.Collectors;

import org.a1cey.bookshelf_pro_application.dto.ReviewDto;
import org.a1cey.bookshelf_pro_application.media_item.review.command.GetAllReviewsCommand;
import org.a1cey.bookshelf_pro_application.media_item.review.result.GetAllReviewsResult;
import org.a1cey.bookshelf_pro_domain.media_item.review.ReviewRepository;
import org.springframework.stereotype.Service;

@Service
public class GetAllReviewsUseCase {
    private final ReviewRepository reviewRepository;

    public GetAllReviewsUseCase(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public GetAllReviewsResult execute(GetAllReviewsCommand command) {
        var reviews = reviewRepository.findByMediaItemId(command.mediaItemId())
                                      .stream()
                                      .map(ReviewDto::from)
                                      .collect(Collectors.toSet());

        return new GetAllReviewsResult(command.mediaItemId().value(), reviews);
    }
}
