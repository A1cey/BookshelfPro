package org.a1cey.bookshelf_pro_domain.media_item;

import org.a1cey.bookshelf_pro_domain.ID;
import org.a1cey.bookshelf_pro_domain.Label;
import org.jmolecules.ddd.annotation.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MediaItemRepository {

    Optional<MediaItem> findByID(ID id);

    List<MediaItem> findAll();

    List<MediaItem> findByTitleContaining(String titleFragment);

    List<MediaItem> findByLabels(List<Label> labels);

}