package org.a1cey.bookshelf_pro_domain.media_item;

import org.a1cey.bookshelf_pro_domain.ID;
import org.jmolecules.ddd.annotation.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MediaItemRepository {

    Optional<MediaItem> findByID(ID id);

    void save(MediaItem mediaItem);

    List<MediaItem> search(MediaItemSearchCriteria searchCriteria);

    void delete(MediaItemID id);

}