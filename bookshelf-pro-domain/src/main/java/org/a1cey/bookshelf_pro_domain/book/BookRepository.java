package org.a1cey.bookshelf_pro_domain.book;

import org.a1cey.bookshelf_pro_domain.ID;
import org.a1cey.bookshelf_pro_domain.PublishPlace;
import org.a1cey.bookshelf_pro_domain.Publisher;
import org.a1cey.bookshelf_pro_domain.media_item.MediaItemRepository;
import org.jmolecules.ddd.annotation.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends MediaItemRepository {

    Book save(Book book);

    Optional<Book> findByISBN(ISBN isbn);

    List<Book> findByAuthors(List<Author> authors);

    List<Book> findByPublishDate(LocalDate publishDate);

    List<Book> findByPublisher(Publisher publisher);

    List<Book> findByPublishPlace(PublishPlace publishPlace);

    List<Book> findByPageCount(PageCount pageCount);

    void deleteByID(ID id);

    void deleteByISBN(ISBN isbn);

}