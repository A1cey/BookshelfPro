package org.a1cey.bookshelf_pro_application.bookshelf.bookshelf_entry.command;

import java.util.Optional;
import java.util.Set;

import org.a1cey.bookshelf_pro_domain.bookshelf.bookshelf_entry.BookshelfEntryId;
import org.a1cey.bookshelf_pro_domain.bookshelf.bookshelf_entry.Label;

public record UpdateBookshelfEntryCommand(
    BookshelfEntryId bookshelfEntryId,
    Optional<Integer> consumptionProgressNumber,
    Optional<Set<Label>> labels
) {}
