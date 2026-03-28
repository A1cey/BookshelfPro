package org.a1cey.bookshelf_pro_domain.review;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.a1cey.bookshelf_pro_domain.OwnershipPolicy;
import org.a1cey.bookshelf_pro_domain.account.AccountId;
import org.a1cey.bookshelf_pro_domain.bookshelf_entry.consumption.ConsumptionProgressSnapshot;
import org.a1cey.bookshelf_pro_domain.bookshelf_entry.consumption.ConsumptionState;
import org.a1cey.bookshelf_pro_domain.media_item.MediaItemId;
import org.jmolecules.ddd.annotation.AggregateRoot;
import org.jmolecules.ddd.annotation.Identity;

import jakarta.validation.Valid;

// TODO: Das Rating einer Playlist, Serie oder Season ergibt sich aus dem durchschnittlichen Rating der darin enthaltenen Media Items + der
//  direkten Reviews. Z.B.: Das Rating einer Season ergibt sich aus dem Durchschnitt der Episoden-Ratings und den direkten Ratings der
//  Season ((SummerEpisodenRatings + SummeDirekteRatings) / (AnzahlEpisodenRatings + AnzahlDirekterRatings)).

@AggregateRoot
public final class Review {

    @Identity
    private final ReviewId id;
    private final MediaItemId mediaItemId;
    private final AccountId owner;
    private final ArrayList<@Valid ReviewChange> reviewHistory = new ArrayList<>();

    private Review(ReviewId id, MediaItemId mediaItemId, AccountId owner, ReviewChange reviewChange) {
        this.id = id;
        this.mediaItemId = mediaItemId;
        this.owner = owner;

        reviewHistory.add(reviewChange);
    }

    private static void validateConsumptionState(ConsumptionProgressSnapshot snapshot) {
        if (snapshot.state() == ConsumptionState.NOT_STARTED) {
            throw new IllegalStateException(
                "Cannot review a medium the user has not started to consume."
            );
        }
    }

    static Review create(
        ReviewId id, MediaItemId mediaItemId, AccountId accountId,
        Rating rating, Comment comment,
        ConsumptionProgressSnapshot consumptionProgressSnapshot
    ) {
        validateConsumptionState(consumptionProgressSnapshot);

        var reviewChange = new ReviewChange(rating, comment, consumptionProgressSnapshot);

        return new Review(id, mediaItemId, accountId, reviewChange);
    }

    public void changeReview(Rating rating, Comment comment, ConsumptionProgressSnapshot consumptionProgressSnapshot,
                             AccountId userRequestingChange) {
        validateConsumptionState(consumptionProgressSnapshot);
        OwnershipPolicy.validate(owner, userRequestingChange, id);

        var reviewChange = new ReviewChange(rating, comment, consumptionProgressSnapshot);

        reviewHistory.add(reviewChange);
    }

    public void changeRating(Rating newRating, ConsumptionProgressSnapshot consumptionProgressSnapshot, AccountId userRequestingChange) {
        changeReview(newRating, comment(), consumptionProgressSnapshot, userRequestingChange);
    }

    public void changeComment(Comment newComment, ConsumptionProgressSnapshot consumptionProgressSnapshot, AccountId userRequestingChange) {
        changeReview(rating(), newComment, consumptionProgressSnapshot, userRequestingChange);
    }

    public ReviewId id() {
        return id;
    }

    public MediaItemId mediaItemId() {
        return mediaItemId;
    }

    public AccountId owner() {
        return owner;
    }

    public Rating rating() {
        return reviewHistory.getLast().rating();
    }

    public Comment comment() {
        return reviewHistory.getLast().comment();
    }

    public LocalDateTime reviewDate() {
        return reviewHistory.getLast().reviewDate();
    }

    public ConsumptionProgressSnapshot consumptionProgress() {
        return reviewHistory.getLast().consumptionProgress();
    }

    public List<ReviewChange> reviewHistory() {
        return Collections.unmodifiableList(reviewHistory);
    }

}
