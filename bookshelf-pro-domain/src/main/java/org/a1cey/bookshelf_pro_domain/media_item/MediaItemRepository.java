package org.a1cey.bookshelf_pro_domain.media_item;

import java.util.List;
import java.util.Optional;

import org.a1cey.bookshelf_pro_domain.Id;
import org.jmolecules.ddd.annotation.Repository;

@Repository
public interface MediaItemRepository {

    Optional<MediaItem> findById(Id id);

    void save(MediaItem mediaItem);

    List<MediaItem> search(MediaItemSearchCriteria searchCriteria);
}