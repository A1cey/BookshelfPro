package org.a1cey.bookshelf_pro_domain.book;

import org.a1cey.bookshelf_pro_domain.ID;
import org.a1cey.bookshelf_pro_domain.media_item.MediaItemRepository;
import org.jmolecules.ddd.annotation.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends MediaItemRepository {

    Book save(Book book);

    Optional<Book> findByISBN(ISBN isbn);

    List<Book> findByAuthors(List<String> authors);

    List<Book> findByPublishDate(LocalDate publishDate);

    List<Book> findByPublisher(String publisher);

    List<Book> findByPublishPlace(String publishPlace);

    List<Book> findByPageCount(int pageCount);

    void deleteByID(ID id);

    void deleteByISBN(ISBN isbn);

}