package org.a1cey.bookshelf_pro_domain.media_item.book;

import java.util.List;
import java.util.Optional;

import org.a1cey.bookshelf_pro_domain.media_item.SpecificSearchCriteria;

public record BookSearchCriteria(
    Optional<Isbn> isbn,
    Optional<List<Author>> authors,
    Optional<PublishDate> publishDate,
    Optional<Publisher> publisher,
    Optional<PublishPlace> publishPlace,
    Optional<PageCount> pageCount
) implements SpecificSearchCriteria {
}
