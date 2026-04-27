package org.a1cey.bookshelf_pro_plugins.db;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
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
import org.a1cey.bookshelf_pro_domain.media_item.movie.Duration;
import org.a1cey.bookshelf_pro_domain.media_item.movie.MovieConsumptionProgress;
import org.a1cey.bookshelf_pro_plugins.db.jooq.tables.records.BookshelfEntryLabelRecord;
import org.a1cey.bookshelf_pro_plugins.db.jooq.tables.records.BookshelfEntryRecord;
import org.a1cey.bookshelf_pro_plugins.db.jooq.tables.records.ConsumptionProgressRecord;
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
        saveLabels(bookshelfEntry.id(), bookshelfEntry.labels());
    }

    @Transactional
    @Override
    public void update(BookshelfEntry bookshelfEntry) {
        updateLabels(bookshelfEntry.id(), bookshelfEntry.labels());
        updateConsumptionProgress(bookshelfEntry.consumptionProgress());
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<BookshelfEntry> findById(BookshelfEntryId bookshelfEntryId) {
        return dsl.fetchOptional(BOOKSHELF_ENTRY, BOOKSHELF_ENTRY.ID.eq(bookshelfEntryId.value()))
                  .map(record -> toBookshelfEntry(
                      record,
                      fetchConsumptionProgress(bookshelfEntryId),
                      fetchLabelsForEntry(bookshelfEntryId)
                  ));
    }

    @Transactional(readOnly = true)
    @Override
    public Set<BookshelfEntry> findByAccount(AccountId accountId) {
        var entryRecords = dsl.fetch(BOOKSHELF_ENTRY, BOOKSHELF_ENTRY.OWNER.eq(accountId.value()));
        if (entryRecords.isEmpty()) {
            return Set.of();
        }

        var entryIds = entryRecords.stream().map(BookshelfEntryRecord::getId).toList();

        var progressByEntryId = fetchConsumptionProgressBatch(entryIds);
        var labelsByEntryId = fetchLabelsBatch(entryIds);

        return entryRecords.stream().map(record -> {
            var progress = progressByEntryId.get(record.getId());
            if (progress == null) {
                throw new IllegalStateException("No ConsumptionProgress found for BookshelfEntry: " + record.getId());
            }
            var labels = labelsByEntryId.getOrDefault(record.getId(), Set.of());
            return toBookshelfEntry(record, progress, labels);
        }).collect(Collectors.toSet());
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<BookshelfEntry> findByAccountAndMediaItem(AccountId accountId, MediaItemId mediaItemId) {
        return dsl.fetchOptional(
            BOOKSHELF_ENTRY,
            BOOKSHELF_ENTRY.OWNER.eq(accountId.value()).and(BOOKSHELF_ENTRY.MEDIA_ITEM_ID.eq(mediaItemId.value()))
        ).map(record -> {
            var id = new BookshelfEntryId(record.getId());
            return toBookshelfEntry(
                record,
                fetchConsumptionProgress(id),
                fetchLabelsForEntry(id)
            );
        });
    }

    @Transactional(readOnly = true)
    @Override
    public boolean existsByAccountAndMediaItem(AccountId accountId, MediaItemId mediaItemId) {
        return dsl.fetchExists(
            BOOKSHELF_ENTRY,
            BOOKSHELF_ENTRY.OWNER.eq(accountId.value())
                                 .and(BOOKSHELF_ENTRY.MEDIA_ITEM_ID.eq(mediaItemId.value()))
        );
    }

    @Transactional(readOnly = true)
    @Override
    public boolean existsByAccountAndId(AccountId accountId, BookshelfEntryId bookshelfEntryId) {
        return dsl.fetchExists(
            BOOKSHELF_ENTRY,
            BOOKSHELF_ENTRY.OWNER.eq(accountId.value())
                                 .and(BOOKSHELF_ENTRY.ID.eq(bookshelfEntryId.value()))
        );
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<LocalDateTime> findLatestConsumptionProgressSnapshotCreationDate(ConsumptionProgressId consumptionProgressId) {
        var date = dsl.select(DSL.max(CONSUMPTION_PROGRESS_SNAPSHOT.CREATED_AT))
                      .from(CONSUMPTION_PROGRESS_SNAPSHOT)
                      .where(CONSUMPTION_PROGRESS_SNAPSHOT.CONSUMPTION_PROGRESS_ID.eq(consumptionProgressId.value()))
                      .fetchOne(DSL.max(CONSUMPTION_PROGRESS_SNAPSHOT.CREATED_AT));
        return Optional.ofNullable(date);
    }

    private BookshelfEntry toBookshelfEntry(BookshelfEntryRecord record, ConsumptionProgress consumptionProgress, Set<Label> labels) {
        return BookshelfEntry
                   .builder(
                       new BookshelfEntryId(record.getId()),
                       new MediaItemId(record.getMediaItemId()),
                       new AccountId(record.getOwner()),
                       consumptionProgress
                   )
                   .labels(labels)
                   .build();
    }

    private void saveLabels(BookshelfEntryId bookshelfEntryId, Set<Label> labels) {
        if (labels.isEmpty()) {
            return;
        }
        dsl.insertInto(BOOKSHELF_ENTRY_LABEL, BOOKSHELF_ENTRY_LABEL.BOOKSHELF_ENTRY_ID, BOOKSHELF_ENTRY_LABEL.LABEL)
           .valuesOfRows(labels.stream().map(label -> DSL.row(bookshelfEntryId.value(), label.name())).toList())
           .execute();
    }

    private void updateLabels(BookshelfEntryId bookshelfEntryId, Set<Label> labels) {
        var existingLabels = dsl.select(BOOKSHELF_ENTRY_LABEL.LABEL)
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

    private Set<Label> fetchLabelsForEntry(BookshelfEntryId bookshelfEntryId) {
        return dsl.fetch(BOOKSHELF_ENTRY_LABEL, BOOKSHELF_ENTRY_LABEL.BOOKSHELF_ENTRY_ID.eq(bookshelfEntryId.value()))
                  .stream()
                  .map(record -> new Label(record.getLabel()))
                  .collect(Collectors.toSet());
    }

    private Map<UUID, Set<Label>> fetchLabelsBatch(List<UUID> entryIds) {
        return dsl.fetch(
                      BOOKSHELF_ENTRY_LABEL,
                      BOOKSHELF_ENTRY_LABEL.BOOKSHELF_ENTRY_ID.in(entryIds)
                  )
                  .stream()
                  .collect(Collectors.groupingBy(
                      BookshelfEntryLabelRecord::getBookshelfEntryId,
                      Collectors.mapping(r -> new Label(r.getLabel()), Collectors.toSet())
                  ));
    }

    private void saveConsumptionProgress(BookshelfEntry bookshelfEntry, MediaItemType mediaItemType) {
        var consumptionProgress = bookshelfEntry.consumptionProgress();
        var stmt = dsl.insertInto(CONSUMPTION_PROGRESS)
                      .set(CONSUMPTION_PROGRESS.ID, consumptionProgress.id().value())
                      .set(CONSUMPTION_PROGRESS.BOOKSHELF_ENTRY_ID, bookshelfEntry.id().value())
                      .set(CONSUMPTION_PROGRESS.STATE, consumptionProgress.state().name())
                      .set(CONSUMPTION_PROGRESS.TYPE, mediaItemType.name());

        switch (consumptionProgress.progress()) {
            case BookConsumptionProgress bookConsumptionProgress ->
                stmt.set(CONSUMPTION_PROGRESS.CURRENT_PAGE, bookConsumptionProgress.current().pageCount())
                    .set(CONSUMPTION_PROGRESS.TOTAL_PAGES, bookConsumptionProgress.total().pageCount()).execute();
            case MovieConsumptionProgress movieConsumptionProgress ->
                stmt.set(CONSUMPTION_PROGRESS.CURRENT_DURATION_SECONDS, toIntSeconds(movieConsumptionProgress.current()))
                    .set(CONSUMPTION_PROGRESS.TOTAL_DURATION_SECONDS, toIntSeconds(movieConsumptionProgress.total()))
                    .execute();
            default -> throw new IllegalStateException("Unexpected consumption progress instance: " + consumptionProgress.getClass());
        }

        saveConsumptionProgressSnapshot(consumptionProgress);
    }

    private void updateConsumptionProgress(ConsumptionProgress progress) {
        var stmt = dsl.update(CONSUMPTION_PROGRESS)
                      .set(CONSUMPTION_PROGRESS.STATE, progress.state().name());

        stmt = switch (progress.progress()) {
            case BookConsumptionProgress bookConsumptionProgress ->
                stmt.set(CONSUMPTION_PROGRESS.CURRENT_PAGE, bookConsumptionProgress.current().pageCount());
            case MovieConsumptionProgress movieConsumptionProgress ->
                stmt.set(CONSUMPTION_PROGRESS.CURRENT_DURATION_SECONDS, toIntSeconds(movieConsumptionProgress.current()));
            default -> throw new IllegalStateException("Unexpected consumption progress instance: " + progress.getClass());
        };
        stmt.where(CONSUMPTION_PROGRESS.ID.eq(progress.id().value())).execute();
        saveConsumptionProgressSnapshot(progress);
    }

    private ConsumptionProgress fetchConsumptionProgress(BookshelfEntryId bookshelfEntryId) {
        var record = dsl.fetchOne(
            CONSUMPTION_PROGRESS,
            CONSUMPTION_PROGRESS.BOOKSHELF_ENTRY_ID.eq(bookshelfEntryId.value())
        );

        if (record == null) {
            throw new IllegalStateException("No ConsumptionProgress found for BookshelfEntry: " + bookshelfEntryId.value());
        }

        return toConsumptionProgress(record);
    }

    private Map<UUID, ConsumptionProgress> fetchConsumptionProgressBatch(List<UUID> entryIds) {
        return dsl.fetch(
                      CONSUMPTION_PROGRESS,
                      CONSUMPTION_PROGRESS.BOOKSHELF_ENTRY_ID.in(entryIds)
                  )
                  .stream()
                  .collect(Collectors.toMap(ConsumptionProgressRecord::getBookshelfEntryId, this::toConsumptionProgress));
    }

    private ConsumptionProgress toConsumptionProgress(ConsumptionProgressRecord record) {
        // SAFETY: reconstruct() is ok, because the values come from DB and were validated on write
        var mediaItemConsumptionProgress = switch (MediaItemType.valueOf(record.getType())) {
            case BOOK -> BookConsumptionProgress.reconstruct(
                new PageCount(record.getCurrentPage()),
                new PageCount(record.getTotalPages())
            );
            case MOVIE -> MovieConsumptionProgress.reconstruct(
                Duration.of(record.getCurrentDurationSeconds()),
                Duration.of(record.getTotalDurationSeconds())
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

        var stmt = dsl.insertInto(CONSUMPTION_PROGRESS_SNAPSHOT)
                      .set(CONSUMPTION_PROGRESS_SNAPSHOT.CONSUMPTION_PROGRESS_ID, consumptionProgress.id().value())
                      .set(CONSUMPTION_PROGRESS_SNAPSHOT.CONSUMPTION_STATE, consumptionProgress.state().name());

        switch (consumptionProgressSnapshot.progress()) {
            case BookConsumptionProgress bookConsumptionProgress ->
                stmt.set(CONSUMPTION_PROGRESS_SNAPSHOT.CURRENT_PAGE, bookConsumptionProgress.current().pageCount())
                    .set(CONSUMPTION_PROGRESS_SNAPSHOT.TOTAL_PAGES, bookConsumptionProgress.total().pageCount())
                    .set(CONSUMPTION_PROGRESS_SNAPSHOT.TYPE, MediaItemType.BOOK.name())
                    .execute();
            case MovieConsumptionProgress movieConsumptionProgress ->
                stmt.set(CONSUMPTION_PROGRESS_SNAPSHOT.CURRENT_DURATION_SECONDS, toIntSeconds(movieConsumptionProgress.current()))
                    .set(CONSUMPTION_PROGRESS_SNAPSHOT.TOTAL_DURATION_SECONDS, toIntSeconds(movieConsumptionProgress.total()))
                    .set(CONSUMPTION_PROGRESS_SNAPSHOT.TYPE, MediaItemType.MOVIE.name())
                    .execute();
            default ->
                throw new IllegalStateException("Unexpected consumption progress instance: " + consumptionProgressSnapshot.getClass());
        }
    }

    private static int toIntSeconds(Duration duration) {
        return Math.toIntExact(duration.time().toSeconds());
    }
}