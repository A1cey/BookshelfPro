package org.a1cey.bookshelf_pro_domain.media_item.review;

import org.a1cey.bookshelf_pro_domain.consumption.ConsumptionProgressID;
import org.a1cey.bookshelf_pro_domain.media_item.MediaItemID;
import org.a1cey.bookshelf_pro_domain.user.UserID;
import org.jmolecules.ddd.annotation.Entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

//Nutzer können Medien bewerten. Eine Bewertung (Review) besteht aus einem Rating im Bereich [0.0, 10.0] und einem optionalen Kommentar.
//Bewertungen sind dabei an den tatsächlichen Konsum gebunden, d.h. eine Bewertung beinhaltet den bei der Erstellung der Bewertung aktuellen
//Fortschritt des Nutzers (z.B. 80% abgeschlossen). Änderungen an Reviews werden historisiert, d.h. es gibt eine aktive (== aktuellste)
// Review
//und eine Historie an Änderungen. So kann ein Nutzer für ein bestimmtes Medium nur eine aktive Review besitzen. Eine Review kann zudem
//nur erstellt werden, wenn der Zustand des Mediums BEGONNEN oder ABGESCHLOSSEN ist.
//Das Rating einer Playlist, Serie oder Season ergibt sich aus dem durchschnittlichen Rating der darin enthaltenen Media Items + der
//direkten Reviews. Z.B.: Das Rating einer Season ergibt sich aus dem Durchschnitt der Episoden-Ratings und den direkten Ratings der
//Season ((SummerEpisodenRatings + SummeDirekteRatings) / (AnzahlEpisodenRatings + AnzahlDirekterRatings)).
//Bewertungen gelten für die globale Bibliothek nicht nur für die private Bibliothek.

@Entity
public final class Review {

    private final ReviewID id;
    private final MediaItemID mediaItemID;
    private final UserID userID;
    private final ArrayList<ReviewChange> reviewHistory = new ArrayList<>();

    private Review(ReviewID id, MediaItemID mediaItemID, UserID userID) {
        this.id = id;
        this.mediaItemID = mediaItemID;
        this.userID = userID;
    }

    public ReviewID id() {
        return id;
    }

    public UserID userID() {
        return userID;
    }

    public void changeReview(ReviewChange newReview) {
        reviewHistory.add(newReview);
    }

    public Optional<Rating> rating() {
        return reviewHistory.getLast().rating();
    }

    public Optional<Comment> comment() {
        return reviewHistory.getLast().comment();
    }

    public Date reviewDate() {
        return reviewHistory.getLast().reviewDate();
    }

    public static ReviewBuilder builder(ReviewID id, MediaItemID mediaItemID, UserID userID, ConsumptionProgressID consumptionProgressID) {
        return new ReviewBuilder(id, mediaItemID, userID, consumptionProgressID);
    }

    public static final class ReviewBuilder {

        private final ReviewID id;
        private final MediaItemID mediaItemID;
        private final UserID userID;
        private final ConsumptionProgressID consumptionProgressID;
        private Optional<Comment> comment;
        private Optional<Rating> rating;

        ReviewBuilder(ReviewID id, MediaItemID mediaItemID, UserID userID, ConsumptionProgressID consumptionProgressID) {
            this.id = id;
            this.mediaItemID = mediaItemID;
            this.userID = userID;
            this.consumptionProgressID = consumptionProgressID;
        }

        public ReviewBuilder comment(Comment comment) {
            this.comment = Optional.of(comment);
            return this;
        }

        public ReviewBuilder rating(Rating rating) {
            this.rating = Optional.of(rating);
            return this;
        }

        public Review build() {
            var review = new Review(id, mediaItemID, userID);
            // TODO: Validate that consumptionProgress.consumptionState is not NOT_STARTED and provide MediaConsumptionProgress to
            //  reviewChange
//            var reviewChange = new ReviewChange(rating, comment);
//            review.changeReview(reviewChange);

            return review;
        }

    }

}
