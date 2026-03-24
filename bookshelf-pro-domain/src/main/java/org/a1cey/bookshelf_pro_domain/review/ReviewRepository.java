package org.a1cey.bookshelf_pro_domain.review;

import org.jmolecules.ddd.annotation.Repository;

import java.util.Optional;

@Repository
public interface ReviewRepository {

    Optional<Review> findById(ReviewID id);

    void save(Review review);

}
