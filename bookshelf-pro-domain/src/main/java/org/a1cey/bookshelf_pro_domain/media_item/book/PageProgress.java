package org.a1cey.bookshelf_pro_domain.media_item.book;

import org.a1cey.bookshelf_pro_domain.consumption.MediaItemConsumptionProgress;
import org.jmolecules.ddd.annotation.ValueObject;

@ValueObject
public record PageProgress(PageCount current, PageCount total) implements MediaItemConsumptionProgress {

    public PageProgress {
        if (current.pageCount() > total.pageCount())
            throw new IllegalArgumentException("Current page count cannot be greater than total page count:\ncurrent:" + current.pageCount() + "\ntotal:" + total);
    }

    @Override
    public double percentage() {return (double) current.pageCount() / (double) total.pageCount();}

    @Override
    public boolean isCompleted() {return current.equals(total);}

    @Override
    public boolean isEmpty() {return current.pageCount() == 0;}

}