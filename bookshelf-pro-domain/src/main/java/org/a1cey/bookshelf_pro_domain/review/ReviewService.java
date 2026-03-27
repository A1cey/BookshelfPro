package org.a1cey.bookshelf_pro_domain.review;

import org.a1cey.bookshelf_pro_domain.account.AccountID;
import org.a1cey.bookshelf_pro_domain.bookshelf_entry.consumption.ConsumptionProgressSnapshot;
import org.a1cey.bookshelf_pro_domain.media_item.MediaItemID;
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
            AccountID accountID,
            Rating rating,
            Comment comment,
            ConsumptionProgressSnapshot snapshot
    ) {
        if (reviewRepository.existsByUserAndMediaItem(accountID, mediaItemID)) {
            throw new IllegalStateException(
                    "User " + accountID + " has already reviewed media item " + mediaItemID
            );
        }

        return Review.create(id, mediaItemID, accountID, rating, comment, snapshot);
    }

}
