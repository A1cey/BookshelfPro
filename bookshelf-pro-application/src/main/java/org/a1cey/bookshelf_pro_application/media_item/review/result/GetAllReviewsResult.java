package org.a1cey.bookshelf_pro_application.media_item.review.result;

import java.util.Set;

import org.a1cey.bookshelf_pro_application.dto.ReviewDto;

public record GetAllReviewsResult(Set<ReviewDto> reviews) {}
