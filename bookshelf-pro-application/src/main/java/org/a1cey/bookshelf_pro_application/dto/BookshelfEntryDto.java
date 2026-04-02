package org.a1cey.bookshelf_pro_application.dto;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.a1cey.bookshelf_pro_domain.bookshelf.bookshelf_entry.BookshelfEntry;
import org.a1cey.bookshelf_pro_domain.bookshelf.bookshelf_entry.Label;

public record BookshelfEntryDto(
    UUID id,
    UUID mediaItemId,
    UUID owner,
    ConsumptionProgressDto consumptionProgress,
    Set<String> labels
) {
    public static BookshelfEntryDto from(BookshelfEntry bookshelfEntry) {
        return new BookshelfEntryDto(
            bookshelfEntry.id().value(),
            bookshelfEntry.mediaItemId().value(),
            bookshelfEntry.owner().value(),
            ConsumptionProgressDto.from(bookshelfEntry.consumptionProgress()),
            bookshelfEntry.labels().stream().map(Label::name).collect(Collectors.toSet())
        );
    }
}
