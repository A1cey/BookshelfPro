package org.a1cey.bookshelf_pro_application.dto;

import java.util.UUID;

import org.a1cey.bookshelf_pro_domain.bookshelf.bookshelf_entry.consumption.ConsumptionProgress;
import org.a1cey.bookshelf_pro_domain.media_item.book.BookConsumptionProgress;
import org.a1cey.bookshelf_pro_domain.media_item.movie.MovieConsumptionProgress;

public record ConsumptionProgressDto(
    UUID id,
    String state,
    MediaItemConsumptionProgressDto progress
) {
    public static ConsumptionProgressDto from(ConsumptionProgress consumptionProgress) {
        var mediaItemConsumptionProgressDto = switch (consumptionProgress.progress()) {
            case BookConsumptionProgress bookConsumptionProgress -> BookConsumptionProgressDto.from(bookConsumptionProgress);
            case MovieConsumptionProgress movieConsumptionProgress -> MovieConsumptionProgressDto.from(movieConsumptionProgress);
            default -> throw new IllegalStateException(
                "Unexpected media item consumption progress type: " + consumptionProgress.progress()
            );
        };

        return new ConsumptionProgressDto(
            consumptionProgress.id().value(),
            consumptionProgress.state().name(),
            mediaItemConsumptionProgressDto
        );
    }
}
