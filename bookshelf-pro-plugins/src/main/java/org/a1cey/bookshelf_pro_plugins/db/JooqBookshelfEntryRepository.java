package org.a1cey.bookshelf_pro_plugins.db;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.a1cey.bookshelf_pro_domain.account.AccountId;
import org.a1cey.bookshelf_pro_domain.bookshelf.bookshelf_entry.BookshelfEntry;
import org.a1cey.bookshelf_pro_domain.bookshelf.bookshelf_entry.BookshelfEntryId;
import org.a1cey.bookshelf_pro_domain.bookshelf.bookshelf_entry.BookshelfEntryRepository;
import org.a1cey.bookshelf_pro_domain.bookshelf.bookshelf_entry.Label;
import org.a1cey.bookshelf_pro_domain.bookshelf.bookshelf_entry.consumption.ConsumptionProgress;
import org.a1cey.bookshelf_pro_domain.bookshelf.bookshelf_entry.consumption.ConsumptionProgressId;
import org.a1cey.bookshelf_pro_domain.bookshelf.bookshelf_entry.consumption.ConsumptionState;
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
import static org.a1cey.bookshelf_pro_plugins.db.jooq.Tables.CONSUMPTION_PROGRESS_SNAPSHOT;

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

    @Transactional
    @Override
    public Optional<BookshelfEntry> findById(BookshelfEntryId bookshelfEntryId) {
        var record = dsl.fetchOne(BOOKSHELF_ENTRY, BOOKSHELF_ENTRY.ID.eq(bookshelfEntryId.value()));

        if (record == null) {
            return Optional.empty();
        }

        var mediaItemId = new MediaItemId(record.getMediaItemId());
        var owner = new AccountId(record.getOwner());
        var consumptionProgress = fetchConsumptionProgress(bookshelfEntryId);
        var labels = fetchLabels(bookshelfEntryId);

        var entry = BookshelfEntry.builder(bookshelfEntryId, mediaItemId, owner, consumptionProgress).labels(labels).build();
        return Optional.of(entry);
    }

    @Transactional
    @Override
    public Set<BookshelfEntry> findByAccount(AccountId accountId) {
        return dsl.fetch(BOOKSHELF_ENTRY, BOOKSHELF_ENTRY.OWNER.eq(accountId.value())).stream().map(record -> {
            var bookshelfEntryId = new BookshelfEntryId(record.getId());
            var mediaItemId = new MediaItemId(record.getMediaItemId());
            var consumptionProgress = fetchConsumptionProgress(bookshelfEntryId);
            var labels = fetchLabels(bookshelfEntryId);

            return BookshelfEntry.builder(bookshelfEntryId, mediaItemId, accountId, consumptionProgress).labels(labels).build();
        }).collect(Collectors.toSet());
    }

    @Transactional
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
        var labels = fetchLabels(bookshelfEntryId);

        var bookshelfEntry = BookshelfEntry.builder(
                bookshelfEntryId,
                mediaItemId,
                accountId,
                consumptionProgress
            ).labels(labels)
             .build();

        return Optional.of(bookshelfEntry);
    }

    @Override
    public boolean existsByAccountAndMediaItem(AccountId accountId, MediaItemId mediaItemId) {
        return dsl.fetchExists(
            BOOKSHELF_ENTRY,
            BOOKSHELF_ENTRY.OWNER.eq(accountId.value())
                                 .and(BOOKSHELF_ENTRY.MEDIA_ITEM_ID.eq(mediaItemId.value()))
        );
    }

    @Override
    public boolean existsByAccountAndId(AccountId accountId, BookshelfEntryId bookshelfEntryId) {
        return dsl.fetchExists(
            BOOKSHELF_ENTRY,
            BOOKSHELF_ENTRY.OWNER.eq(accountId.value())
                                 .and(BOOKSHELF_ENTRY.ID.eq(bookshelfEntryId.value()))
        );
    }

    @Transactional
    @Override
    public void update(BookshelfEntry bookshelfEntry) {
        updateLabels(bookshelfEntry.id(), bookshelfEntry.labels());
        updateConsumptionProgress(bookshelfEntry.consumptionProgress());
    }

    @Override
    public Optional<LocalDateTime> findLatestConsumptionProgressSnapshotCreationDate(ConsumptionProgressId consumptionProgressId) {
        var date = dsl.select(DSL.max(CONSUMPTION_PROGRESS_SNAPSHOT.CREATED_AT))
                      .from(CONSUMPTION_PROGRESS_SNAPSHOT)
                      .where(CONSUMPTION_PROGRESS_SNAPSHOT.CONSUMPTION_PROGRESS_ID.eq(consumptionProgressId.value()))
                      .fetchOne(DSL.max(CONSUMPTION_PROGRESS_SNAPSHOT.CREATED_AT));

        return Optional.ofNullable(date);
    }

    private void saveLabels(BookshelfEntry bookshelfEntry) {
        if (bookshelfEntry.labels().isEmpty()) {
            return;
        }

        dsl.insertInto(BOOKSHELF_ENTRY_LABEL, BOOKSHELF_ENTRY_LABEL.BOOKSHELF_ENTRY_ID, BOOKSHELF_ENTRY_LABEL.LABEL)
           .valuesOfRows(
               bookshelfEntry
                   .labels()
                   .stream()
                   .map(label -> DSL.row(bookshelfEntry.id().value(), label.name()))
                   .toList()
           ).execute();
    }

    private void updateLabels(BookshelfEntryId bookshelfEntryId, Set<Label> labels) {
        if (labels.isEmpty()) {
            return;
        }

        var existingLabels = dsl
                                 .select(BOOKSHELF_ENTRY_LABEL.LABEL)
                                 .from(BOOKSHELF_ENTRY_LABEL)
                                 .where(BOOKSHELF_ENTRY_LABEL.BOOKSHELF_ENTRY_ID.eq(bookshelfEntryId.value()))
                                 .fetchSet(BOOKSHELF_ENTRY_LABEL.LABEL);

        var newLabels = labels.stream().map(Label::name).collect(Collectors.toSet());

        var labelsToDelete = existingLabels.stream().filter(label -> !newLabels.contains(label)).toList();
        var labelsToInsert = newLabels.stream().filter(label -> !existingLabels.contains(label)).toList();

        if (!labelsToDelete.isEmpty()) {
            dsl.deleteFrom(BOOKSHELF_ENTRY_LABEL)
               .where(BOOKSHELF_ENTRY_LABEL.BOOKSHELF_ENTRY_ID.eq(bookshelfEntryId.value()))
               .and(BOOKSHELF_ENTRY_LABEL.LABEL.in(labelsToDelete))
               .execute();
        }

        if (!labelsToInsert.isEmpty()) {
            dsl.insertInto(BOOKSHELF_ENTRY_LABEL, BOOKSHELF_ENTRY_LABEL.BOOKSHELF_ENTRY_ID, BOOKSHELF_ENTRY_LABEL.LABEL)
               .valuesOfRows(labelsToInsert.stream().map(label -> DSL.row(bookshelfEntryId.value(), label)).toList())
               .execute();
        }
    }

    private Set<Label> fetchLabels(BookshelfEntryId bookshelfEntryId) {
        return dsl.fetch(BOOKSHELF_ENTRY_LABEL, BOOKSHELF_ENTRY_LABEL.BOOKSHELF_ENTRY_ID.eq(bookshelfEntryId.value()))
                  .stream()
                  .map(labelRecord -> new Label(labelRecord.getLabel()))
                  .collect(Collectors.toSet());
    }

    private void saveConsumptionProgress(BookshelfEntry bookshelfEntry, MediaItemType mediaItemType) {
        var statement = dsl.insertInto(CONSUMPTION_PROGRESS)
                           .set(CONSUMPTION_PROGRESS.ID, bookshelfEntry.consumptionProgress().id().value())
                           .set(CONSUMPTION_PROGRESS.BOOKSHELF_ENTRY_ID, bookshelfEntry.id().value())
                           .set(CONSUMPTION_PROGRESS.STATE, bookshelfEntry.consumptionProgress().state().name())
                           .set(CONSUMPTION_PROGRESS.TYPE, mediaItemType.name());

        var consumptionProgress = bookshelfEntry.consumptionProgress();

        switch (consumptionProgress.progress()) {
            case BookConsumptionProgress bookConsumptionProgress ->
                statement.set(CONSUMPTION_PROGRESS.CURRENT_PAGE, bookConsumptionProgress.current().pageCount())
                         .set(CONSUMPTION_PROGRESS.TOTAL_PAGES, bookConsumptionProgress.total().pageCount()).execute();
            default ->
                throw new IllegalStateException("Unexpected consumption progress type: " + bookshelfEntry.consumptionProgress().progress());
        }

        saveConsumptionProgressSnapshot(consumptionProgress);
    }

    private void updateConsumptionProgress(ConsumptionProgress progress) {
        var statement = dsl.update(CONSUMPTION_PROGRESS).set(CONSUMPTION_PROGRESS.STATE, progress.state().name());

        statement = switch (progress.progress()) {
            case BookConsumptionProgress bookConsumptionProgress ->
                statement.set(CONSUMPTION_PROGRESS.CURRENT_PAGE, bookConsumptionProgress.current().pageCount());
            default -> throw new IllegalStateException("Unexpected consumption progress type: " + progress.progress());
        };

        statement.where(CONSUMPTION_PROGRESS.ID.eq(progress.id().value())).execute();

        saveConsumptionProgressSnapshot(progress);
    }

    private ConsumptionProgress fetchConsumptionProgress(BookshelfEntryId bookshelfEntryId) {
        var record = dsl.fetchOne(
            CONSUMPTION_PROGRESS,
            CONSUMPTION_PROGRESS.BOOKSHELF_ENTRY_ID.eq(bookshelfEntryId.value())
        );

        if (record == null) {
            throw new IllegalStateException("No ConsumptionProgress found for BookshelfEntry with id: " + bookshelfEntryId.value());
        }

        // SAFETY: Creating a MediaItemConsumptionProgress via its constructor is safe because the total time comes from the DB and
        // was already checked when the row was created.
        var mediaItemConsumptionProgress = switch (MediaItemType.valueOf(record.getType())) {
            case MediaItemType.BOOK -> BookConsumptionProgress.reconstruct(
                new PageCount(record.getCurrentPage()),
                new PageCount(record.getTotalPages())
            );
        };

        return new ConsumptionProgress(
            new ConsumptionProgressId(record.getId()),
            mediaItemConsumptionProgress,
            ConsumptionState.valueOf(record.getState())
        );
    }

    private void saveConsumptionProgressSnapshot(ConsumptionProgress consumptionProgress) {
        var consumptionProgressSnapshot = consumptionProgress.snapshot();

        var statement = dsl.insertInto(CONSUMPTION_PROGRESS_SNAPSHOT)
                           .set(CONSUMPTION_PROGRESS_SNAPSHOT.CONSUMPTION_PROGRESS_ID, consumptionProgress.id().value())
                           .set(CONSUMPTION_PROGRESS_SNAPSHOT.CONSUMPTION_STATE, consumptionProgress.state().name());

        switch (consumptionProgressSnapshot.progress()) {
            case BookConsumptionProgress bookConsumptionProgress ->
                statement.set(CONSUMPTION_PROGRESS_SNAPSHOT.CURRENT_PAGE, bookConsumptionProgress.current().pageCount())
                         .set(CONSUMPTION_PROGRESS_SNAPSHOT.TOTAL_PAGES, bookConsumptionProgress.total().pageCount())
                         .set(CONSUMPTION_PROGRESS_SNAPSHOT.TYPE, MediaItemType.BOOK.name())
                         .execute();
            default -> throw new IllegalStateException("Unexpected media consumption progress type: " + consumptionProgress.progress());
        }
    }

}
