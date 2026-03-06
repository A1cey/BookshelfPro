package org.a1cey.bookshelfpro.domain;

import org.jmolecules.ddd.annotation.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository {

    Book save(Book book);

    Optional<Book> findByID(ID id);

    Optional<Book> findByISBN(ISBN isbn);

    List<Book> findByTitleContaining(String titleFragment);

    List<Book> findByAuthors(List<String> authors);

    List<Book> findByPublishDate(LocalDate publishDate);

    List<Book> findByPublisher(String publisher);

    List<Book> findByPublishPlace(String publishPlace);

    List<Book> findByPageCount(int pageCount);

    List<Book> findAll();

    void deleteByID(ID id);

    void deleteByISBN(ISBN isbn);
}