package org.a1cey.bookshelf_pro_domain.bookshelf_entry;

import java.util.Optional;

import org.a1cey.bookshelf_pro_domain.account.AccountId;
import org.a1cey.bookshelf_pro_domain.bookshelf_entry.consumption.ConsumptionProgressSnapshot;
import org.a1cey.bookshelf_pro_domain.media_item.MediaItemId;
import org.a1cey.bookshelf_pro_domain.media_item.MediaItemType;
import org.jmolecules.ddd.annotation.Repository;

@Repository
public interface BookshelfEntryRepository {
    void save(BookshelfEntry bookshelfEntry, MediaItemType mediaItemType);

    Optional<BookshelfEntry> findByAccountAndMediaItem(AccountId accountId, MediaItemId mediaItemId);

    boolean existsByAccountAndMediaItem(AccountId accountId, MediaItemId mediaItemId);

    Optional<ConsumptionProgressSnapshot> findLatestConsumptionSnapshot(BookshelfEntryId bookshelfEntryId);

}
