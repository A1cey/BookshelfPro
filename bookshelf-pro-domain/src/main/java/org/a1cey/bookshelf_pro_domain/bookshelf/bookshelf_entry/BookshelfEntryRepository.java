package org.a1cey.bookshelf_pro_domain.bookshelf.bookshelf_entry;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

import org.a1cey.bookshelf_pro_domain.account.AccountId;
import org.a1cey.bookshelf_pro_domain.bookshelf.bookshelf_entry.consumption.ConsumptionProgressId;
import org.a1cey.bookshelf_pro_domain.bookshelf.bookshelf_entry.consumption.ConsumptionProgressSnapshot;
import org.a1cey.bookshelf_pro_domain.media_item.MediaItemId;
import org.a1cey.bookshelf_pro_domain.media_item.MediaItemType;
import org.jmolecules.ddd.annotation.Repository;

@Repository
public interface BookshelfEntryRepository {
    void save(BookshelfEntry bookshelfEntry, MediaItemType mediaItemType);

    Optional<BookshelfEntry> findById(BookshelfEntryId bookshelfEntryId);

    Set<BookshelfEntry> findByAccount(AccountId accountId);

    Optional<BookshelfEntry> findByAccountAndMediaItem(AccountId accountId, MediaItemId mediaItemId);

    LocalDateTime findLatestConsumptionProgressSnapshotCreationDate(ConsumptionProgressId consumptionProgressId);

    boolean existsByAccountAndMediaItem(AccountId accountId, MediaItemId mediaItemId);

    Optional<ConsumptionProgressSnapshot> findLatestConsumptionSnapshot(BookshelfEntryId bookshelfEntryId);

    void update(BookshelfEntry bookshelfEntry);
}
