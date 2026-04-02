package org.a1cey.bookshelf_pro_application.dto;

import java.time.LocalDateTime;

import org.a1cey.bookshelf_pro_domain.media_item.review.ReviewChange;

public record ReviewChangeDto(
    float rating,
    String comment,
    LocalDateTime reviewDate,
    ConsumptionProgressSnapshotDto consumptionProgressSnapshot
) {
    public static ReviewChangeDto from(ReviewChange reviewChange) {
        return new ReviewChangeDto(
            reviewChange.rating().rating(),
            reviewChange.comment().comment(),
            reviewChange.reviewDate(),
            ConsumptionProgressSnapshotDto.from(reviewChange.consumptionProgressSnapshot())
        );
    }
}
