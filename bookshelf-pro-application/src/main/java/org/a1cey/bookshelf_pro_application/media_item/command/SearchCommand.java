package org.a1cey.bookshelf_pro_application.media_item.command;

import org.a1cey.bookshelf_pro_domain.media_item.MediaItemSearchCriteria;

public record SearchCommand(MediaItemSearchCriteria mediaItemSearchCriteria) {}
