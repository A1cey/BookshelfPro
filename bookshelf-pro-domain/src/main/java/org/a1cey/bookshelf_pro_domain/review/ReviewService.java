package org.a1cey.bookshelf_pro_domain.review;

import org.a1cey.bookshelf_pro_domain.bookshelf_entry.consumption.ConsumptionProgressSnapshot;
import org.a1cey.bookshelf_pro_domain.media_item.MediaItemID;
import org.a1cey.bookshelf_pro_domain.user.UserID;
import org.jmolecules.ddd.annotation.Service;

@Service
public final class ReviewService {

    private final ReviewRepository reviewRepository;

    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public Review addReview(
            ReviewID id,
            MediaItemID mediaItemID,
            UserID userID,
            Rating rating,
            Comment comment,
            ConsumptionProgressSnapshot snapshot
    ) {
        if (reviewRepository.existsByUserAndMediaItem(userID, mediaItemID)) {
            throw new IllegalStateException(
                    "User " + userID + " has already reviewed media item " + mediaItemID
            );
        }

        return Review.create(id, mediaItemID, userID, rating, comment, snapshot);
    }

}
