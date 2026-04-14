package org.a1cey.bookshelf_pro_domain.media_item.book;

import org.a1cey.bookshelf_pro_domain.bookshelf.bookshelf_entry.consumption.MediaItemConsumptionProgress;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jmolecules.ddd.annotation.ValueObject;

import jakarta.validation.Valid;

@ValueObject
public final class BookConsumptionProgress implements MediaItemConsumptionProgress {

    private final PageCount current;
    private final PageCount total;

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
        return (double) current.pageCount() / total.pageCount();
    }

    @Override
    public boolean isCompleted() {
        return current.equals(total);
    }

    @Override
    public boolean isStarted() {
        return current.pageCount() > 0 && current.pageCount() < total.pageCount();
    }

    @Override
    public boolean isNotStarted() {
        return current.pageCount() == 0 && total.pageCount() != 0;
    }

    @Override
    public BookConsumptionProgress update(long newCurrent) {
        return new BookConsumptionProgress(new PageCount((int) newCurrent), total);
    }

    /**
     * Only use this method if you know that total is valid for the Book this is created for.
     * Otherwise, use Book.createProgress.
     * Example:
     * Reading the progress from the DB must give you a valid total. Therefore, you can use this method.
     */
    public static BookConsumptionProgress reconstruct(PageCount current, PageCount total) {
        return new BookConsumptionProgress(current, total);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof BookConsumptionProgress other)) {
            return false;
        }

        return total.equals(other.total()) && current.equals(other.current);
    }

    @Override
    public int hashCode() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        hashCodeBuilder.append(current);
        hashCodeBuilder.append(total);
        return hashCodeBuilder.toHashCode();
    }
}