package org.a1cey.bookshelf_pro_domain.bookshelf.bookshelf_entry.consumption;

public interface MediaItemConsumptionProgress {

    double percentage();

    boolean isCompleted();

    boolean isStarted();

    boolean isNotStarted();

    /**
     * Create a new MediaItemConsumptionProgress that is valid for the same media item.
     * Provide the new current consumption value.
     */
    MediaItemConsumptionProgress update(int newCurrent);
}
