package org.a1cey.bookshelf_pro_domain;

import java.util.UUID;

import org.a1cey.bookshelf_pro_domain.account.AccountId;
import org.a1cey.bookshelf_pro_domain.bookshelf.bookshelf_entry.consumption.ConsumptionProgressSnapshot;
import org.a1cey.bookshelf_pro_domain.bookshelf.bookshelf_entry.consumption.ConsumptionState;
import org.a1cey.bookshelf_pro_domain.media_item.MediaItemId;
import org.a1cey.bookshelf_pro_domain.media_item.book.BookConsumptionProgress;
import org.a1cey.bookshelf_pro_domain.media_item.book.PageCount;
import org.a1cey.bookshelf_pro_domain.media_item.review.Comment;
import org.a1cey.bookshelf_pro_domain.media_item.review.Rating;
import org.a1cey.bookshelf_pro_domain.media_item.review.Review;
import org.a1cey.bookshelf_pro_domain.media_item.review.ReviewId;
import org.a1cey.bookshelf_pro_domain.media_item.review.ReviewRepository;
import org.a1cey.bookshelf_pro_domain.media_item.review.ReviewService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class ReviewTest {

    @Mock
    private ReviewRepository reviewRepository;

    @InjectMocks
    private ReviewService reviewService;

    private ConsumptionProgressSnapshot startedSnapshot() {
        var progress = BookConsumptionProgress.reconstruct(new PageCount(50), new PageCount(200));
        return new ConsumptionProgressSnapshot(ConsumptionState.STARTED, progress);
    }

    private ConsumptionProgressSnapshot notStartedSnapshot() {
        var progress = BookConsumptionProgress.reconstruct(new PageCount(0), new PageCount(200));
        return new ConsumptionProgressSnapshot(ConsumptionState.NOT_STARTED, progress);
    }

    private Review createReview(Rating rating, Comment comment, ConsumptionProgressSnapshot snapshot) {
        var id = new ReviewId(UUID.randomUUID());
        var mediaItemId = new MediaItemId(UUID.randomUUID());
        var owner = new AccountId(UUID.randomUUID());
        return reviewService.addReview(id, mediaItemId, owner, rating, comment, snapshot);
    }

    @Test
    void createReviewWithStartedState() {
        var rating = new Rating(7.5f);
        var comment = new Comment("Great book");
        var review = createReview(rating, comment, startedSnapshot());
        assertEquals(rating.rating(), review.rating().rating());
        assertEquals(comment.comment(), review.comment().comment());
    }

    @Test
    void createWithNotStartedStateThrows() {
        var rating = new Rating(7.5f);
        var comment = new Comment("Great book");
        assertThrows(IllegalArgumentException.class, () -> createReview(rating, comment, notStartedSnapshot()));
    }

    @Test
    void ratingAboveMaxClampsTo10() {
        var rating = new Rating(15f);
        assertEquals(10.0f, rating.rating());
    }

    @Test
    void ratingBelowMinClampsTo0() {
        var rating = new Rating(-5f);
        assertEquals(0.0f, rating.rating());
    }

    @Test
    void ratingIsRoundedToOneDecimal() {
        var rating = new Rating(7.55f);
        assertEquals(7.6f, rating.rating(), 0.001f);
    }
}
