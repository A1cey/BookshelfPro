package org.a1cey.bookshelf_pro_domain.media_item.review;

import java.util.Optional;
import java.util.Set;

import org.a1cey.bookshelf_pro_domain.account.AccountId;
import org.a1cey.bookshelf_pro_domain.bookshelf.bookshelf_entry.BookshelfEntry;
import org.a1cey.bookshelf_pro_domain.media_item.MediaItemId;
import org.jmolecules.ddd.annotation.Repository;

@Repository
public interface ReviewRepository {

    Optional<Review> findById(ReviewId id);

    Set<Review> findByOwner(AccountId accountId);

    Set<Review> findByMediaItemId(MediaItemId mediaItemId);

    boolean existsByUserAndMediaItem(AccountId accountId, MediaItemId mediaItemId);

    void save(Review review, BookshelfEntry bookshelfEntry);

    void update(Review review, BookshelfEntry bookshelfEntry);

    void delete(ReviewId reviewId);

}
