package org.a1cey.bookshelf_pro_domain.review;

import org.a1cey.bookshelf_pro_domain.account.AccountID;
import org.a1cey.bookshelf_pro_domain.media_item.MediaItemID;
import org.jmolecules.ddd.annotation.Repository;

import java.util.Optional;

@Repository
public interface ReviewRepository {

    Optional<Review> findById(ReviewID id);

    boolean existsByUserAndMediaItem(AccountID accountID, MediaItemID mediaItemID);

    void save(Review review);

    void delete(ReviewID reviewID);

}
