package org.a1cey.bookshelf_pro_domain.media_item;

import java.util.Optional;
import java.util.Set;

import org.a1cey.bookshelf_pro_domain.Id;
import org.jmolecules.ddd.annotation.Repository;

@Repository
public interface MediaItemRepository {

    Optional<MediaItem> findById(Id id);

    Set<? extends MediaItem> findByType(MediaItemType type);

    void save(MediaItem mediaItem);

    void update(MediaItem mediaItem);

    Set<? extends MediaItem> search(MediaItemSearchCriteria searchCriteria);
}