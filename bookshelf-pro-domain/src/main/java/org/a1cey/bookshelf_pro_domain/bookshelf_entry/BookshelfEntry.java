package org.a1cey.bookshelf_pro_domain.bookshelf_entry;

import jakarta.validation.Valid;
import org.a1cey.bookshelf_pro_domain.OwnershipPolicy;
import org.a1cey.bookshelf_pro_domain.account.AccountID;
import org.a1cey.bookshelf_pro_domain.bookshelf_entry.consumption.ConsumptionProgress;
import org.a1cey.bookshelf_pro_domain.bookshelf_entry.consumption.MediaItemConsumptionProgress;
import org.a1cey.bookshelf_pro_domain.media_item.MediaItemID;
import org.jmolecules.ddd.annotation.AggregateRoot;
import org.jmolecules.ddd.annotation.Identity;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@AggregateRoot
public final class BookshelfEntry {

    @Identity
    private final BookshelfEntryID id;
    private final MediaItemID mediaItemID;
    private final AccountID owner;
    @Valid
    private final ConsumptionProgress consumptionProgress;
    private final Set<@Valid Label> labels;

    private BookshelfEntry(BookshelfEntryID id, MediaItemID mediaItemID, AccountID owner, ConsumptionProgress consumptionProgress, Set<@Valid Label> labels) {
        this.id = id;
        this.mediaItemID = mediaItemID;
        this.owner = owner;
        this.consumptionProgress = consumptionProgress;
        this.labels = labels;
    }

    public BookshelfEntryID id() {
        return id;
    }

    public AccountID owner() {
        return owner;
    }

    public MediaItemID mediaItemID() {
        return mediaItemID;
    }

    public ConsumptionProgress consumptionProgress() {
        return consumptionProgress;
    }

    public void updateConsumptionProgress(@Valid MediaItemConsumptionProgress newProgress, AccountID userRequestingChange) {
        OwnershipPolicy.validate(owner, userRequestingChange, id);
        consumptionProgress.updateProgress(newProgress);
    }

    public Set<Label> labels() {
        return Collections.unmodifiableSet(labels);
    }

    public void addLabel(@Valid Label label, AccountID userRequestingChange) {
        OwnershipPolicy.validate(owner, userRequestingChange, id);
        labels.add(label);
    }

    public void removeLabel(@Valid Label label, AccountID userRequestingChange) {
        OwnershipPolicy.validate(owner, userRequestingChange, id);
        labels.remove(label);
    }

    public static BookshelfEntryBuilder builder(BookshelfEntryID id, MediaItemID mediaItemID, AccountID owner, ConsumptionProgress consumptionProgress) {
        return new BookshelfEntryBuilder(id, mediaItemID, owner, consumptionProgress);
    }

    public static final class BookshelfEntryBuilder {

        private final BookshelfEntryID id;
        private final MediaItemID mediaItemID;
        private final AccountID owner;
        @Valid
        private final ConsumptionProgress consumptionProgress;
        private final Set<@Valid Label> labels = new HashSet<>();

        private BookshelfEntryBuilder(BookshelfEntryID id, MediaItemID mediaItemID, AccountID owner, ConsumptionProgress consumptionProgress) {
            this.id = id;
            this.mediaItemID = mediaItemID;
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
            return new BookshelfEntry(id, mediaItemID, owner, consumptionProgress, labels);
        }

    }

}