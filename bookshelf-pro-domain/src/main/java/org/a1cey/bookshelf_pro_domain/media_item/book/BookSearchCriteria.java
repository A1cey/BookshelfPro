package org.a1cey.bookshelf_pro_domain.media_item.book;

import org.a1cey.bookshelf_pro_domain.media_item.SpecificSearchCriteria;
import org.a1cey.bookshelf_pro_domain.media_item.Subtitle;

import java.util.List;
import java.util.Optional;

public record BookSearchCriteria(
        Optional<ISBN> isbn,
        Optional<Subtitle> subtitle,
        Optional<List<Author>> authors,
        Optional<PublishDate> publishDate,
        Optional<Publisher> publisher,
        Optional<PublishPlace> publishPlace,
        Optional<PageCount> pageCount
) implements SpecificSearchCriteria {
}
