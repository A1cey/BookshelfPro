package org.a1cey.bookshelf_pro_domain.review;

import jakarta.validation.Valid;
import org.a1cey.bookshelf_pro_domain.bookshelf_entry.consumption.ConsumptionProgressSnapshot;
import org.a1cey.bookshelf_pro_domain.bookshelf_entry.consumption.ConsumptionState;
import org.a1cey.bookshelf_pro_domain.bookshelf_entry.consumption.MediaItemConsumptionProgress;
import org.a1cey.bookshelf_pro_domain.media_item.MediaItemID;
import org.a1cey.bookshelf_pro_domain.user.UserID;
import org.jmolecules.ddd.annotation.AggregateRoot;
import org.jmolecules.ddd.annotation.Identity;

import java.util.ArrayList;
import java.util.Date;

//TODO: Das Rating einer Playlist, Serie oder Season ergibt sich aus dem durchschnittlichen Rating der darin enthaltenen Media Items + der
// direkten Reviews. Z.B.: Das Rating einer Season ergibt sich aus dem Durchschnitt der Episoden-Ratings und den direkten Ratings der
// Season ((SummerEpisodenRatings + SummeDirekteRatings) / (AnzahlEpisodenRatings + AnzahlDirekterRatings)).

@AggregateRoot
public final class Review {

    @Identity
    private final ReviewID id;
    private final MediaItemID mediaItemID;
    private final UserID userID;
    private final ArrayList<@Valid ReviewChange> reviewHistory = new ArrayList<>();

    private Review(ReviewID id, MediaItemID mediaItemID, UserID userID, ReviewChange reviewChange) {
        this.id = id;
        this.mediaItemID = mediaItemID;
        this.userID = userID;

        reviewHistory.add(reviewChange);
    }

    public static Review create(
            ReviewID id, MediaItemID mediaItemID, UserID userID,
            Rating rating, Comment comment,
            ConsumptionProgressSnapshot consumptionProgressSnapshot
    ) {
        if (consumptionProgressSnapshot.state() == ConsumptionState.NOT_STARTED) {
            throw new IllegalStateException("Cannot review a medium the user has not started to consume.");
        }

        var reviewChange = new ReviewChange(rating, comment, consumptionProgressSnapshot.progress());

        return new Review(id, mediaItemID, userID, reviewChange);
    }

    public void changeReview(Rating rating, Comment comment, ConsumptionProgressSnapshot consumptionProgressSnapshot) {
        if (consumptionProgressSnapshot.state() == ConsumptionState.NOT_STARTED) {
            throw new IllegalStateException("Cannot review a medium the user has not started to consume.");
        }

        var reviewChange = new ReviewChange(rating, comment, consumptionProgressSnapshot.progress());

        reviewHistory.add(reviewChange);
    }

    public void changeRating(Rating newRating, ConsumptionProgressSnapshot consumptionProgressSnapshot) {
        changeReview(newRating, comment(), consumptionProgressSnapshot);
    }

    public void changeComment(Comment newComment, ConsumptionProgressSnapshot consumptionProgressSnapshot) {
        changeReview(rating(), newComment, consumptionProgressSnapshot);
    }

    public ReviewID id() {
        return id;
    }

    public MediaItemID mediaItemID() {return mediaItemID;}

    public UserID userID() {
        return userID;
    }

    public Rating rating() {
        return reviewHistory.getLast().rating();
    }

    public Comment comment() {
        return reviewHistory.getLast().comment();
    }

    public Date reviewDate() {
        return reviewHistory.getLast().reviewDate();
    }

    public MediaItemConsumptionProgress consumptionProgress() {
        return reviewHistory.getLast().consumptionProgress();
    }

}
