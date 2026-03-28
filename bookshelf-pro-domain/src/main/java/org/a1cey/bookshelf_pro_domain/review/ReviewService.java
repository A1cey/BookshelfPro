package org.a1cey.bookshelf_pro_domain.review;

import org.a1cey.bookshelf_pro_domain.account.AccountId;
import org.a1cey.bookshelf_pro_domain.bookshelf_entry.consumption.ConsumptionProgressSnapshot;
import org.a1cey.bookshelf_pro_domain.media_item.MediaItemId;
import org.jmolecules.ddd.annotation.Service;

@Service
public final class ReviewService {

    private final ReviewRepository reviewRepository;

    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public Review addReview(
        ReviewId id,
        MediaItemId mediaItemId,
        AccountId accountId,
        Rating rating,
        Comment comment,
        ConsumptionProgressSnapshot snapshot
    ) {
        if (reviewRepository.existsByUserAndMediaItem(accountId, mediaItemId)) {
            throw new IllegalStateException(
                "User " + accountId + " has already reviewed media item " + mediaItemId
            );
        }

        return Review.create(id, mediaItemId, accountId, rating, comment, snapshot);
    }

}
