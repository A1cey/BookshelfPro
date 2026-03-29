package org.a1cey.bookshelf_pro_domain.media_item.book;

import org.a1cey.bookshelf_pro_domain.bookshelf_entry.consumption.MediaItemConsumptionProgress;
import org.jmolecules.ddd.annotation.ValueObject;

import jakarta.validation.Valid;

@ValueObject
public final class BookConsumptionProgress implements MediaItemConsumptionProgress {

    private final PageCount current;
    private final PageCount total;

    // This is package private to be used in Book.createProgress
    BookConsumptionProgress(@Valid PageCount current, @Valid PageCount total) {
        if (current.pageCount() > total.pageCount()) {
            throw new IllegalArgumentException(
                "Current page count cannot be greater than total page count:\ncurrent:" + current.pageCount() + "\ntotal:" + total
            );
        }
        this.current = current;
        this.total = total;
    }

    public PageCount current() {
        return current;
    }

    public PageCount total() {
        return total;
    }

    @Override
    public double percentage() {
        return (double) current.pageCount() / (double) total.pageCount();
    }

    @Override
    public boolean isCompleted() {
        return current.equals(total);
    }

    @Override
    public boolean isStarted() {
        return current.pageCount() > 0 && current.pageCount() < total.pageCount();
    }

}