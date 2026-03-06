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

    Optional<Book> findByTitleContaining(String titleFragment);

    Optional<Book> findByAuthors(List<String> authors);

    Optional<Book> findByPublishDate(LocalDate publishDate);

    Optional<Book> findByPublisher(String publisher);

    Optional<Book> findByPublishPlace(String publishPlace);

    Optional<Book> findByPageCount(int pageCount);

    List<Book> findAll();

    void deleteByID(ID id);

    void deleteByISBN(ISBN isbn);

}