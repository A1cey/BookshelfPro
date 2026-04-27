package org.a1cey.bookshelf_pro_plugins.db;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.a1cey.bookshelf_pro_domain.account.AccountId;
import org.a1cey.bookshelf_pro_domain.bookshelf.bookshelf_entry.BookshelfEntry;
import org.a1cey.bookshelf_pro_domain.bookshelf.bookshelf_entry.consumption.ConsumptionProgressSnapshot;
import org.a1cey.bookshelf_pro_domain.bookshelf.bookshelf_entry.consumption.ConsumptionState;
import org.a1cey.bookshelf_pro_domain.media_item.MediaItemId;
import org.a1cey.bookshelf_pro_domain.media_item.MediaItemType;
import org.a1cey.bookshelf_pro_domain.media_item.book.BookConsumptionProgress;
import org.a1cey.bookshelf_pro_domain.media_item.book.PageCount;
import org.a1cey.bookshelf_pro_domain.media_item.movie.Duration;
import org.a1cey.bookshelf_pro_domain.media_item.movie.MovieConsumptionProgress;
import org.a1cey.bookshelf_pro_domain.media_item.review.Comment;
import org.a1cey.bookshelf_pro_domain.media_item.review.Rating;
import org.a1cey.bookshelf_pro_domain.media_item.review.Review;
import org.a1cey.bookshelf_pro_domain.media_item.review.ReviewChange;
import org.a1cey.bookshelf_pro_domain.media_item.review.ReviewId;
import org.a1cey.bookshelf_pro_domain.media_item.review.ReviewRepository;
import org.a1cey.bookshelf_pro_plugins.db.jooq.tables.records.ConsumptionProgressSnapshotRecord;
import org.a1cey.bookshelf_pro_plugins.db.jooq.tables.records.ReviewChangeRecord;
import org.a1cey.bookshelf_pro_plugins.db.jooq.tables.records.ReviewRecord;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
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

    @Transactional(readOnly = true)
    @Override
    public Optional<Review> findById(ReviewId id) {
        return dsl.fetchOptional(REVIEW, REVIEW.ID.eq(id.value()))
                  .map(record -> {
                      var reviewId = new ReviewId(record.getId());
                      var mediaItemId = new MediaItemId(record.getMediaItemId());
                      var owner = new AccountId(record.getOwner());
                      var reviewChanges = fetchReviewChanges(List.of(reviewId.value())).getOrDefault(reviewId.value(), List.of());

                      return Review.reconstruct(reviewId, mediaItemId, owner, reviewChanges);
                  });
    }

    @Transactional(readOnly = true)
    @Override
    public Set<Review> findByOwner(AccountId owner) {
        var records = dsl.fetch(REVIEW, REVIEW.OWNER.eq(owner.value()));
        return recreateReviews(records);
    }

    @Transactional(readOnly = true) // FIX: was missing @Transactional entirely
    @Override
    public Set<Review> findByMediaItemId(MediaItemId mediaItemId) {
        var records = dsl.fetch(REVIEW, REVIEW.MEDIA_ITEM_ID.eq(mediaItemId.value()));
        return recreateReviews(records);
    }

    @Override
    public boolean existsByUserAndMediaItem(AccountId accountId, MediaItemId mediaItemId) {
        return dsl.fetchExists(REVIEW, REVIEW.MEDIA_ITEM_ID.eq(mediaItemId.value()).and(REVIEW.OWNER.eq(accountId.value())));
    }

    @Transactional
    @Override
    public void save(Review review, BookshelfEntry bookshelfEntry) {
        dsl.insertInto(REVIEW)
           .set(REVIEW.ID, review.id().value())
           .set(REVIEW.MEDIA_ITEM_ID, review.mediaItemId().value())
           .set(REVIEW.OWNER, review.owner().value())
           .execute();

        saveReviewChange(review, bookshelfEntry);
    }

    @Transactional
    @Override
    public void update(Review review, BookshelfEntry bookshelfEntry) {
        saveReviewChange(review, bookshelfEntry);
    }

    @Transactional
    @Override
    public void delete(ReviewId reviewId) {
        dsl.delete(REVIEW_CHANGE).where(REVIEW_CHANGE.REVIEW_ID.eq(reviewId.value())).execute();
        dsl.delete(REVIEW).where(REVIEW.ID.eq(reviewId.value())).execute();
    }

    private Set<Review> recreateReviews(List<? extends ReviewRecord> reviewRecords) {
        if (reviewRecords.isEmpty()) {
            return Set.of();
        }

        var reviewIds = reviewRecords.stream().map(ReviewRecord::getId).toList();
        var changesByReviewId = fetchReviewChanges(reviewIds);

        return reviewRecords.stream().map(record -> {
            var reviewId = new ReviewId(record.getId());
            return Review.reconstruct(
                reviewId,
                new MediaItemId(record.getMediaItemId()),
                new AccountId(record.getOwner()),
                changesByReviewId.getOrDefault(record.getId(), List.of())
            );
        }).collect(Collectors.toSet());
    }

    private void saveReviewChange(Review review, BookshelfEntry bookshelfEntry) {
        var progressId = bookshelfEntry.consumptionProgress().id().value();

        var creationDate = dsl
                               .select(DSL.max(CONSUMPTION_PROGRESS_SNAPSHOT.CREATED_AT))
                               .from(CONSUMPTION_PROGRESS_SNAPSHOT)
                               .where(CONSUMPTION_PROGRESS_SNAPSHOT.CONSUMPTION_PROGRESS_ID.eq(progressId))
                               .fetchOne(DSL.max(CONSUMPTION_PROGRESS_SNAPSHOT.CREATED_AT));

        if (creationDate == null) {
            throw new IllegalStateException("No snapshot creation date for consumption progress with id: " + progressId);
        }

        dsl.insertInto(REVIEW_CHANGE)
           .set(REVIEW_CHANGE.REVIEW_ID, review.id().value())
           .set(REVIEW_CHANGE.COMMENT, review.comment().comment())
           .set(REVIEW_CHANGE.RATING, (double) review.rating().rating())
           .set(REVIEW_CHANGE.REVIEW_DATE, review.reviewDate())
           .set(REVIEW_CHANGE.CONSUMPTION_PROGRESS_SNAPSHOT_CONSUMPTION_PROGRESS_ID, progressId)
           .set(REVIEW_CHANGE.CONSUMPTION_PROGRESS_SNAPSHOT_CREATED_AT, creationDate)
           .execute();
    }

    private Map<UUID, List<ReviewChange>> fetchReviewChanges(List<UUID> reviewIds) {
        var reviewHistory = dsl.fetch(REVIEW_CHANGE, REVIEW_CHANGE.REVIEW_ID.in(reviewIds));

        if (reviewHistory.isEmpty()) {
            return Map.of();
        }

        var snapshotKeys = reviewHistory.stream()
                                        .map(record -> DSL.row(
                                            record.getConsumptionProgressSnapshotConsumptionProgressId(),
                                            record.getConsumptionProgressSnapshotCreatedAt()
                                        ))
                                        .toList();

        var snapshotMap = dsl.fetch(
                                 CONSUMPTION_PROGRESS_SNAPSHOT,
                                 DSL.row(CONSUMPTION_PROGRESS_SNAPSHOT.CONSUMPTION_PROGRESS_ID, CONSUMPTION_PROGRESS_SNAPSHOT.CREATED_AT)
                                    .in(snapshotKeys)
                             ).stream()
                             .collect(Collectors.toMap(
                                 record -> new SnapshotKey(record.getConsumptionProgressId(), record.getCreatedAt()),
                                 record -> record
                             ));

        return reviewHistory.stream().collect(Collectors.groupingBy(
            ReviewChangeRecord::getReviewId,
            Collectors.mapping(
                reviewChangeRecord -> {
                    var key = new SnapshotKey(
                        reviewChangeRecord.getConsumptionProgressSnapshotConsumptionProgressId(),
                        reviewChangeRecord.getConsumptionProgressSnapshotCreatedAt()
                    );
                    var snapshot = snapshotMap.get(key);
                    if (snapshot == null) {
                        throw new IllegalStateException("No snapshot found for progress id: " + key.progressId());
                    }
                    return toReviewChange(reviewChangeRecord, snapshot);
                },
                Collectors.toList()
            )
        ));
    }

    private ReviewChange toReviewChange(ReviewChangeRecord changeRecord, ConsumptionProgressSnapshotRecord snapshotRecord) {
        var consumptionState = ConsumptionState.valueOf(snapshotRecord.getConsumptionState());

        // SAFETY: reconstruct() is ok, because values come from DB and were validated on write
        var mediaItemConsumptionProgress = switch (MediaItemType.valueOf(snapshotRecord.getType())) {
            case BOOK -> BookConsumptionProgress.reconstruct(
                new PageCount(snapshotRecord.getCurrentPage()),
                new PageCount(snapshotRecord.getTotalPages())
            );
            case MOVIE -> MovieConsumptionProgress.reconstruct(
                Duration.of(snapshotRecord.getCurrentDurationSeconds()),
                Duration.of(snapshotRecord.getTotalDurationSeconds())
            );
        };

        return new ReviewChange(
            new Rating(changeRecord.getRating().floatValue()),
            new Comment(changeRecord.getComment()),
            changeRecord.getReviewDate(),
            new ConsumptionProgressSnapshot(consumptionState, mediaItemConsumptionProgress)
        );
    }

    private record SnapshotKey(UUID progressId, LocalDateTime createdAt) {}
}