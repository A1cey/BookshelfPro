package org.a1cey.bookshelf_pro_domain.review;

import org.a1cey.bookshelf_pro_domain.media_item.MediaItemID;
import org.a1cey.bookshelf_pro_domain.user.UserID;
import org.jmolecules.ddd.annotation.Repository;

import java.util.Optional;

@Repository
public interface ReviewRepository {

    Optional<Review> findById(ReviewID id);

    boolean existsByUserAndMediaItem(UserID userID, MediaItemID mediaItemID);

    void save(Review review);

}
