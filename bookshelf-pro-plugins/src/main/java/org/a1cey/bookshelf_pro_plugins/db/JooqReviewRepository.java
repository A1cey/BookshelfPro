package org.a1cey.bookshelf_pro_plugins.db;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.a1cey.bookshelf_pro_domain.account.AccountId;
import org.a1cey.bookshelf_pro_domain.bookshelf.bookshelf_entry.consumption.ConsumptionProgressSnapshot;
import org.a1cey.bookshelf_pro_domain.bookshelf.bookshelf_entry.consumption.ConsumptionState;
import org.a1cey.bookshelf_pro_domain.media_item.MediaItemId;
import org.a1cey.bookshelf_pro_domain.media_item.MediaItemType;
import org.a1cey.bookshelf_pro_domain.media_item.book.BookConsumptionProgress;
import org.a1cey.bookshelf_pro_domain.media_item.book.PageCount;
import org.a1cey.bookshelf_pro_domain.media_item.review.Comment;
import org.a1cey.bookshelf_pro_domain.media_item.review.Rating;
import org.a1cey.bookshelf_pro_domain.media_item.review.Review;
import org.a1cey.bookshelf_pro_domain.media_item.review.ReviewChange;
import org.a1cey.bookshelf_pro_domain.media_item.review.ReviewId;
import org.a1cey.bookshelf_pro_domain.media_item.review.ReviewRepository;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import static org.a1cey.bookshelf_pro_plugins.db.jooq.Tables.CONSUMPTION_PROGRESS_SNAPSHOT;
import static org.a1cey.bookshelf_pro_plugins.db.jooq.Tables.REVIEW_CHANGE;
import static org.a1cey.bookshelf_pro_plugins.db.jooq.tables.Review.REVIEW;

@Repository
public class JooqReviewRepository implements ReviewRepository {

    private final DSLContext dsl;

    public JooqReviewRepository(DSLContext dsl) {
        this.dsl = dsl;
    }

    @Override
    public Optional<Review> findById(ReviewId id) {
        var record = dsl.fetchOne(REVIEW, REVIEW.ID.eq(id.value()));

        if (record == null) {
            return Optional.empty();
        }

        var reviewId = new ReviewId(record.getId());
        var mediaItemId = new MediaItemId(record.getMediaItemId());
        var owner = new AccountId(record.getOwner());
        var reviewChanges = fetchReviewChanges(reviewId);

        return Optional.of(Review.reconstruct(reviewId, mediaItemId, owner, reviewChanges));
    }

    @Override
    public boolean existsByUserAndMediaItem(AccountId accountId, MediaItemId mediaItemId) {
        return dsl.fetchExists(
            REVIEW,
            REVIEW.MEDIA_ITEM_ID
                .eq(mediaItemId.value())
                .and(REVIEW.OWNER.eq(accountId.value()))
        );
    }

    @Transactional
    @Override
    public void save(Review review) {
        dsl.insertInto(REVIEW)
           .set(REVIEW.ID, review.id().value())
           .set(REVIEW.MEDIA_ITEM_ID, review.mediaItemId().value())
           .set(REVIEW.OWNER, review.owner().value()).execute();

        var consumptionProgressSnapshotId = fetchConsumptionProgressSnapshotId(review.consumptionProgressSnapshot());

        saveReviewChange(review, consumptionProgressSnapshotId);
    }

    @Transactional
    @Override
    public void delete(ReviewId reviewId) {
        dsl.delete(REVIEW).where(REVIEW.ID.eq(reviewId.value())).execute();
    }

    private void saveReviewChange(Review review, UUID consumption_progress_snapshot_id) {
        dsl.insertInto(REVIEW_CHANGE)
           .set(REVIEW_CHANGE.REVIEW_ID, review.id().value())
           .set(REVIEW_CHANGE.COMMENT, review.comment().comment())
           .set(REVIEW_CHANGE.RATING, (double) review.rating().rating())
           .set(REVIEW_CHANGE.REVIEW_DATE, review.reviewDate())
           .set(REVIEW_CHANGE.CONSUMPTION_PROGRESS_SNAPSHOT_ID, consumption_progress_snapshot_id)
           .execute();
    }

    private List<ReviewChange> fetchReviewChanges(ReviewId reviewId) {
        return dsl.fetch(REVIEW_CHANGE, REVIEW_CHANGE.REVIEW_ID.eq(reviewId.value()))
                  .stream()
                  .map(reviewChange -> {
                      var rating = new Rating(reviewChange.getRating().floatValue());
                      var comment = new Comment(reviewChange.getComment());
                      var reviewDate = reviewChange.getReviewDate();
                      var consumptionProgressSnapshotRecord = dsl.fetchOne(
                          CONSUMPTION_PROGRESS_SNAPSHOT,
                          CONSUMPTION_PROGRESS_SNAPSHOT.ID.eq(reviewChange.getConsumptionProgressSnapshotId())
                      );

                      if (consumptionProgressSnapshotRecord == null) {
                          throw new IllegalStateException(
                              "No Consumption Progress Snapshot found for review change with id: " + reviewChange.getId()
                          );
                      }

                      var consumptionState = ConsumptionState.valueOf(
                          consumptionProgressSnapshotRecord.getConsumptionState()
                      );

                      // SAFETY: Creating a MediaItemConsumptionProgress via its constructor is safe because the total value comes from
                      // the DB and was already checked when the row was created.
                      var mediaItemConsumptionProgress = switch (MediaItemType.valueOf(
                          consumptionProgressSnapshotRecord.getType()
                      )) {
                          case MediaItemType.BOOK -> BookConsumptionProgress.reconstruct(
                              new PageCount(consumptionProgressSnapshotRecord.getCurrentPage()),
                              new PageCount(consumptionProgressSnapshotRecord.getTotalPages())
                          );
                      };

                      var consumptionProgressSnapshot = new ConsumptionProgressSnapshot(consumptionState, mediaItemConsumptionProgress);
                      return new ReviewChange(rating, comment, reviewDate, consumptionProgressSnapshot);
                  }).toList();
    }

    private UUID fetchConsumptionProgressSnapshotId(ConsumptionProgressSnapshot consumptionProgressSnapshot) {
        dsl.fetchOne(CONSUMPTION_PROGRESS_SNAPSHOT, CONSUMPTION_PROGRESS_SNAPSHOT.)
    }
}
