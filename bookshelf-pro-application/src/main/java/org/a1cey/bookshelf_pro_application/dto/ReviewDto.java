package org.a1cey.bookshelf_pro_application.dto;

import java.util.List;
import java.util.UUID;

import org.a1cey.bookshelf_pro_domain.media_item.review.Review;

public record ReviewDto(
    UUID id,
    UUID mediaItemId,
    UUID owner,
    List<ReviewChangeDto> reviewHistory
) {
    public static ReviewDto from(Review review) {
        return new ReviewDto(
            review.id().value(),
            review.mediaItemId().value(),
            review.owner().value(),
            review.reviewHistory().stream().map(ReviewChangeDto::from).toList()
        );
    }
}
