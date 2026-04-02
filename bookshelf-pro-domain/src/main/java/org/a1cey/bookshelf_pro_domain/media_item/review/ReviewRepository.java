package org.a1cey.bookshelf_pro_domain.media_item.review;

import java.util.Optional;

import org.a1cey.bookshelf_pro_domain.account.AccountId;
import org.a1cey.bookshelf_pro_domain.bookshelf.bookshelf_entry.BookshelfEntry;
import org.a1cey.bookshelf_pro_domain.media_item.MediaItemId;
import org.jmolecules.ddd.annotation.Repository;

@Repository
public interface ReviewRepository {

    Optional<Review> findById(ReviewId id);

    boolean existsByUserAndMediaItem(AccountId accountId, MediaItemId mediaItemId);

    void save(Review review, BookshelfEntry bookshelfEntry);

    void delete(ReviewId reviewId);

}
