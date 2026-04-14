package org.a1cey.bookshelf_pro_application.dto;

import java.time.Duration;

import org.a1cey.bookshelf_pro_domain.media_item.movie.MovieConsumptionProgress;

public record MovieConsumptionProgressDto(Duration watched, Duration total) implements MediaItemConsumptionProgressDto {
    public static MovieConsumptionProgressDto from(MovieConsumptionProgress movieConsumptionProgress) {
        return new MovieConsumptionProgressDto(movieConsumptionProgress.current().time(), movieConsumptionProgress.total().time());
    }
}
