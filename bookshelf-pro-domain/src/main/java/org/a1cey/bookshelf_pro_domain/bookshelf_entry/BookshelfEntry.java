package org.a1cey.bookshelf_pro_domain.bookshelf_entry;

import jakarta.validation.Valid;
import org.a1cey.bookshelf_pro_domain.OwnershipPolicy;
import org.a1cey.bookshelf_pro_domain.bookshelf_entry.consumption.ConsumptionProgress;
import org.a1cey.bookshelf_pro_domain.bookshelf_entry.consumption.MediaItemConsumptionProgress;
import org.a1cey.bookshelf_pro_domain.media_item.MediaItemID;
import org.a1cey.bookshelf_pro_domain.user.UserID;
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
    private final UserID owner;
    @Valid
    private final ConsumptionProgress consumptionProgress;
    private final Set<@Valid Label> labels;

    private BookshelfEntry(BookshelfEntryID id, MediaItemID mediaItemID, UserID owner, ConsumptionProgress consumptionProgress, Set<@Valid Label> labels) {
        this.id = id;
        this.mediaItemID = mediaItemID;
        this.owner = owner;
        this.consumptionProgress = consumptionProgress;
        this.labels = labels;
    }

    public BookshelfEntryID id() {
        return id;
    }

    public UserID owner() {
        return owner;
    }

    public MediaItemID mediaItemID() {
        return mediaItemID;
    }

    public ConsumptionProgress consumptionProgress() {
        return consumptionProgress;
    }

    public void updateConsumptionProgress(@Valid MediaItemConsumptionProgress newProgress, UserID userRequestingChange) {
        OwnershipPolicy.validate(owner, userRequestingChange, id.id());
        consumptionProgress.updateProgress(newProgress);
    }

    public Set<Label> labels() {
        return Collections.unmodifiableSet(labels);
    }

    public void addLabel(@Valid Label label, UserID userRequestingChange) {
        OwnershipPolicy.validate(owner, userRequestingChange, id.id());
        labels.add(label);
    }

    public void removeLabel(@Valid Label label, UserID userRequestingChange) {
        OwnershipPolicy.validate(owner, userRequestingChange, id.id());
        labels.remove(label);
    }

    public static BookshelfEntryBuilder builder(BookshelfEntryID id, MediaItemID mediaItemID, UserID owner, ConsumptionProgress consumptionProgress) {
        return new BookshelfEntryBuilder(id, mediaItemID, owner, consumptionProgress);
    }

    public static final class BookshelfEntryBuilder {

        private final BookshelfEntryID id;
        private final MediaItemID mediaItemID;
        private final UserID owner;
        @Valid
        private final ConsumptionProgress consumptionProgress;
        private final Set<@Valid Label> labels = new HashSet<>();

        private BookshelfEntryBuilder(BookshelfEntryID id, MediaItemID mediaItemID, UserID owner, ConsumptionProgress consumptionProgress) {
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