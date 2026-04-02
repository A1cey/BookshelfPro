package org.a1cey.bookshelf_pro_application.dto;

import org.a1cey.bookshelf_pro_domain.bookshelf.bookshelf_entry.consumption.ConsumptionProgressSnapshot;
import org.a1cey.bookshelf_pro_domain.media_item.book.BookConsumptionProgress;

public record ConsumptionProgressSnapshotDto(
    String consumptionProgressState,
    MediaItemConsumptionProgressDto progress
) {
    public static ConsumptionProgressSnapshotDto from(ConsumptionProgressSnapshot snapshot) {
        var consumptionProgressDto = switch (snapshot.progress()) {
            case BookConsumptionProgress bookConsumptionProgress -> BookConsumptionProgressDto.from(bookConsumptionProgress);
            default -> throw new IllegalStateException("Unexpected media item consumption progress type: " + snapshot.progress());
        };

        return new ConsumptionProgressSnapshotDto(snapshot.state().name(), consumptionProgressDto);
    }
}
