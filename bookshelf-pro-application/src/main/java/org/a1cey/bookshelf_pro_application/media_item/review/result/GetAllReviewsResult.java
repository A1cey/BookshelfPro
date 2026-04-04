package org.a1cey.bookshelf_pro_application.media_item.review.result;

import java.util.Set;
import java.util.UUID;

import org.a1cey.bookshelf_pro_application.dto.ReviewDto;

public record GetAllReviewsResult(
    UUID mediaItemId,
    Set<ReviewDto> reviews
) {}
