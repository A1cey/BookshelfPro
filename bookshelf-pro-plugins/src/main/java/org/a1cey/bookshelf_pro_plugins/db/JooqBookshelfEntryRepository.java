package org.a1cey.bookshelf_pro_plugins.db;

import java.util.Optional;

import org.a1cey.bookshelf_pro_domain.account.AccountId;
import org.a1cey.bookshelf_pro_domain.bookshelf_entry.BookshelfEntry;
import org.a1cey.bookshelf_pro_domain.bookshelf_entry.BookshelfEntryId;
import org.a1cey.bookshelf_pro_domain.bookshelf_entry.BookshelfEntryRepository;
import org.a1cey.bookshelf_pro_domain.bookshelf_entry.consumption.ConsumptionProgress;
import org.a1cey.bookshelf_pro_domain.bookshelf_entry.consumption.ConsumptionProgressId;
import org.a1cey.bookshelf_pro_domain.bookshelf_entry.consumption.ConsumptionProgressSnapshot;
import org.a1cey.bookshelf_pro_domain.media_item.MediaItemId;
import org.a1cey.bookshelf_pro_domain.media_item.MediaItemType;
import org.a1cey.bookshelf_pro_domain.media_item.book.BookConsumptionProgress;
import org.a1cey.bookshelf_pro_domain.media_item.book.PageCount;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import static org.a1cey.bookshelf_pro_plugins.db.jooq.Tables.BOOKSHELF_ENTRY;
import static org.a1cey.bookshelf_pro_plugins.db.jooq.Tables.BOOKSHELF_ENTRY_LABEL;
import static org.a1cey.bookshelf_pro_plugins.db.jooq.Tables.CONSUMPTION_PROGRESS;

@Repository
public class JooqBookshelfEntryRepository implements BookshelfEntryRepository {
    private final DSLContext dsl;

    public JooqBookshelfEntryRepository(DSLContext dsl) {
        this.dsl = dsl;
    }

    @Transactional
    @Override
    public void save(BookshelfEntry bookshelfEntry, MediaItemType mediaItemType) {
        dsl.insertInto(BOOKSHELF_ENTRY)
           .set(BOOKSHELF_ENTRY.ID, bookshelfEntry.id().value())
           .set(BOOKSHELF_ENTRY.MEDIA_ITEM_ID, bookshelfEntry.mediaItemId().value())
           .set(BOOKSHELF_ENTRY.OWNER, bookshelfEntry.owner().value())
           .execute();

        saveConsumptionProgress(bookshelfEntry, mediaItemType);
        saveLabels(bookshelfEntry);
    }

    @Override
    public Optional<BookshelfEntry> findByAccountAndMediaItem(AccountId accountId, MediaItemId mediaItemId) {
        var bookshelfEntryRecord = dsl.fetchOne(
            BOOKSHELF_ENTRY,
            BOOKSHELF_ENTRY.OWNER.eq(accountId.value())
                                 .and(BOOKSHELF_ENTRY.MEDIA_ITEM_ID.eq(mediaItemId.value()))
        );

        if (bookshelfEntryRecord == null) {
            return Optional.empty();
        }

        var bookshelfEntryId = new BookshelfEntryId(bookshelfEntryRecord.getId());
        var consumptionProgress = fetchConsumptionProgress(bookshelfEntryId);

        var bookshelfEntry = BookshelfEntry.builder(
            bookshelfEntryId,
            mediaItemId,
            accountId,
            consumptionProgress
        ).build();

        return Optional.of(bookshelfEntry);
    }

    @Override
    public boolean existsByAccountAndMediaItem(AccountId accountId, MediaItemId mediaItemId) {
        return dsl.fetchExists(
            BOOKSHELF_ENTRY,
            BOOKSHELF_ENTRY.ID.eq(accountId.value())
                              .and(BOOKSHELF_ENTRY.MEDIA_ITEM_ID.eq(mediaItemId.value()))
        );
    }

    @Override
    public Optional<ConsumptionProgressSnapshot> findLatestConsumptionSnapshot(BookshelfEntryId bookshelfEntryId) {
        return Optional.empty();
    }

    private void saveConsumptionProgress(BookshelfEntry bookshelfEntry, MediaItemType mediaItemType) {
        var statement = dsl.insertInto(CONSUMPTION_PROGRESS)
                           .set(CONSUMPTION_PROGRESS.ID, bookshelfEntry.consumptionProgress().id().value())
                           .set(CONSUMPTION_PROGRESS.BOOKSHELF_ENTRY_ID, bookshelfEntry.id().value())
                           .set(CONSUMPTION_PROGRESS.STATE, bookshelfEntry.consumptionProgress().state().name())
                           .set(CONSUMPTION_PROGRESS.TYPE, mediaItemType.name());

        switch (bookshelfEntry.consumptionProgress().progress()) {
            case BookConsumptionProgress bookConsumptionProgress ->
                statement.set(CONSUMPTION_PROGRESS.CURRENT_PAGE, bookConsumptionProgress.current().pageCount())
                         .set(CONSUMPTION_PROGRESS.TOTAL_PAGES, bookConsumptionProgress.total().pageCount()).execute();
            default ->
                throw new IllegalStateException("Unexpected consumption progress type: " + bookshelfEntry.consumptionProgress().progress());
        }
    }

    private void saveLabels(BookshelfEntry bookshelfEntry) {
        dsl.insertInto(BOOKSHELF_ENTRY_LABEL, BOOKSHELF_ENTRY_LABEL.BOOKSHELF_ENTRY_ID, BOOKSHELF_ENTRY_LABEL.LABEL)
           .valuesOfRows(
               bookshelfEntry
                   .labels()
                   .stream()
                   .map(label -> DSL.row(bookshelfEntry.id().value(), label.name()))
                   .toList()
           ).execute();
    }

    private ConsumptionProgress fetchConsumptionProgress(BookshelfEntryId bookshelfEntryId) {
        var record = dsl.fetchOne(
            CONSUMPTION_PROGRESS,
            CONSUMPTION_PROGRESS.BOOKSHELF_ENTRY_ID.eq(bookshelfEntryId.value())
        );

        if (record == null) {
            throw new IllegalStateException("No ConsumptionProgress found for BookshelfEntry with id: " + bookshelfEntryId.value());
        }

        // SAFETY: Creating a MediaItemConsumptionProgress via its constructor is safe because the total value comes from the DB and
        // was already checked when the row was created.
        var mediaItemConsumptionProgress = switch (MediaItemType.valueOf(record.getType())) {
            case MediaItemType.BOOK -> BookConsumptionProgress.reconstruct(
                new PageCount(record.getCurrentPage()),
                new PageCount(record.getTotalPages())
            );
        };

        return new ConsumptionProgress(
            new ConsumptionProgressId(record.getId()),
            mediaItemConsumptionProgress
        );
    }
}
