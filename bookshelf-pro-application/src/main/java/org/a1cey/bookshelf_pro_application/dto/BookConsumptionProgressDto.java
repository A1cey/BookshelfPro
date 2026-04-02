package org.a1cey.bookshelf_pro_application.dto;

import org.a1cey.bookshelf_pro_domain.media_item.book.BookConsumptionProgress;

public record BookConsumptionProgressDto(int current, int total) implements MediaItemConsumptionProgressDto {
    public static BookConsumptionProgressDto from(BookConsumptionProgress bookConsumptionProgress) {
        return new BookConsumptionProgressDto(bookConsumptionProgress.current().pageCount(), bookConsumptionProgress.total().pageCount());
    }
}
