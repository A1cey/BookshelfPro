package org.a1cey.bookshelf_pro_domain.bookshelf_entry;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.a1cey.bookshelf_pro_domain.OwnershipPolicy;
import org.a1cey.bookshelf_pro_domain.account.AccountId;
import org.a1cey.bookshelf_pro_domain.bookshelf_entry.consumption.ConsumptionProgress;
import org.a1cey.bookshelf_pro_domain.bookshelf_entry.consumption.MediaItemConsumptionProgress;
import org.a1cey.bookshelf_pro_domain.media_item.MediaItemId;
import org.jmolecules.ddd.annotation.AggregateRoot;
import org.jmolecules.ddd.annotation.Identity;

import jakarta.validation.Valid;

@AggregateRoot
public final class BookshelfEntry {

    @Identity
    private final BookshelfEntryId id;
    private final MediaItemId mediaItemId;
    private final AccountId owner;
    @Valid
    private final ConsumptionProgress consumptionProgress;
    private final Set<@Valid Label> labels;

    private BookshelfEntry(
        BookshelfEntryId id, MediaItemId mediaItemId, AccountId owner, ConsumptionProgress consumptionProgress, Set<@Valid Label> labels
    ) {
        this.id = id;
        this.mediaItemId = mediaItemId;
        this.owner = owner;
        this.consumptionProgress = consumptionProgress;
        this.labels = labels;
    }

    public static BookshelfEntryBuilder builder(
        BookshelfEntryId id, MediaItemId mediaItemId, AccountId owner, ConsumptionProgress consumptionProgress
    ) {
        return new BookshelfEntryBuilder(id, mediaItemId, owner, consumptionProgress);
    }

    public BookshelfEntryId id() {
        return id;
    }

    public AccountId owner() {
        return owner;
    }

    public MediaItemId mediaItemId() {
        return mediaItemId;
    }

    public ConsumptionProgress consumptionProgress() {
        return consumptionProgress;
    }

    public void updateConsumptionProgress(@Valid MediaItemConsumptionProgress newProgress, AccountId userRequestingChange) {
        OwnershipPolicy.validate(owner, userRequestingChange, id);
        consumptionProgress.updateProgress(newProgress);
    }

    public Set<Label> labels() {
        return Collections.unmodifiableSet(labels);
    }

    public void addLabel(@Valid Label label, AccountId userRequestingChange) {
        OwnershipPolicy.validate(owner, userRequestingChange, id);
        labels.add(label);
    }

    public void removeLabel(@Valid Label label, AccountId userRequestingChange) {
        OwnershipPolicy.validate(owner, userRequestingChange, id);
        labels.remove(label);
    }

    public static final class BookshelfEntryBuilder {

        private final BookshelfEntryId id;
        private final MediaItemId mediaItemId;
        private final AccountId owner;
        @Valid
        private final ConsumptionProgress consumptionProgress;
        private final Set<@Valid Label> labels = new HashSet<>();

        private BookshelfEntryBuilder(
            final BookshelfEntryId id, final MediaItemId mediaItemId, final AccountId owner, final ConsumptionProgress consumptionProgress
        ) {
            this.id = id;
            this.mediaItemId = mediaItemId;
            this.owner = owner;
            this.consumptionProgress = consumptionProgress;
        }

        public BookshelfEntryBuilder labels(Set<@Valid Label> labels) {
            this.labels.addAll(labels);
            return this;
        }

        public BookshelfEntryBuilder labels(@Valid Label label) {
            labels.add(label);
            return this;
        }

        public BookshelfEntry build() {
            return new BookshelfEntry(id, mediaItemId, owner, consumptionProgress, labels);
        }

    }

}