package org.a1cey.bookshelf_pro_application.bookshelf.bookshelf_entry.command;

import java.util.Set;

import org.a1cey.bookshelf_pro_domain.bookshelf.bookshelf_entry.Label;
import org.a1cey.bookshelf_pro_domain.media_item.MediaItemId;

public record AddBookshelfEntryCommand(
    MediaItemId mediaItemId,
    Set<Label> labels
) {}
