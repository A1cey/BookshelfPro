package org.a1cey.bookshelf_pro_domain.bookshelf_entry;

import jakarta.validation.Valid;
import org.a1cey.bookshelf_pro_domain.bookshelf_entry.consumption.ConsumptionProgress;
import org.a1cey.bookshelf_pro_domain.media_item.MediaItemID;
import org.a1cey.bookshelf_pro_domain.user.UserID;
import org.jmolecules.ddd.annotation.AggregateRoot;
import org.jmolecules.ddd.annotation.Identity;

//TODO: Is this really an aggregate root?
@AggregateRoot
public record BookshelfEntry(
        @Identity BookshelfEntryID id,
        MediaItemID mediaItemID,
        @Valid ConsumptionProgress consumptionProgress,
        UserID owner
) {
}
