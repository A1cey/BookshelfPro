package org.a1cey.bookshelfpro.domain;

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