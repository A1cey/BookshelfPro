package org.a1cey.bookshelf_pro_domain.bookshelf_entry.consumption;

public record ConsumptionProgressSnapshot(
        ConsumptionState state,
        MediaItemConsumptionProgress progress
) {

    public static ConsumptionProgressSnapshot of(ConsumptionProgress cp) {
        return new ConsumptionProgressSnapshot(cp.state(), cp.progress());
    }

}