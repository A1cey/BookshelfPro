package org.a1cey.bookshelf_pro_domain.bookshelf_entry;

import org.a1cey.bookshelf_pro_domain.consumption.ConsumptionProgress;
import org.a1cey.bookshelf_pro_domain.media_item.MediaItemID;
import org.jmolecules.ddd.annotation.AggregateRoot;

@AggregateRoot
public record BookshelfEntry(BookshelfEntryID id, MediaItemID mediaItemID, ConsumptionProgress consumptionProgress) {
}
