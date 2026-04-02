package org.a1cey.bookshelf_pro_domain.bookshelf.bookshelf_entry.consumption;

public interface MediaItemConsumptionProgress {

    double percentage();

    boolean isCompleted();

    boolean isStarted();
}
