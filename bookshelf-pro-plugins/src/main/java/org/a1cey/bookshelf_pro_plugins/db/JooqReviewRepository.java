package org.a1cey.bookshelf_pro_plugins.db;

import java.util.Optional;

import org.a1cey.bookshelf_pro_domain.account.AccountId;
import org.a1cey.bookshelf_pro_domain.media_item.MediaItemId;
import org.a1cey.bookshelf_pro_domain.review.Review;
import org.a1cey.bookshelf_pro_domain.review.ReviewId;
import org.a1cey.bookshelf_pro_domain.review.ReviewRepository;
import org.springframework.stereotype.Repository;

@Repository
public class JooqReviewRepository implements ReviewRepository {
    @Override
    public Optional<Review> findById(ReviewId id) {
        // TODO:
        return Optional.empty();
    }

    @Override
    public boolean existsByUserAndMediaItem(AccountId accountId, MediaItemId mediaItemId) {
        // TODO:
        return false;
    }

    @Override
    public void save(Review review) {
        // TODO:
    }

    @Override
    public void delete(ReviewId reviewId) {
        // TODO:
    }
}
