package org.a1cey.bookshelfpro.domain;

public sealed interface ConsumptionProgress permits PageProgress {

    double percentage();

    boolean isCompleted();

    boolean isEmpty();

}
