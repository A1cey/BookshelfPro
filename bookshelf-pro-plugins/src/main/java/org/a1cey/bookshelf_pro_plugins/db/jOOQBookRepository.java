package org.a1cey.bookshelf_pro_plugins.db;


import org.a1cey.bookshelf_pro_domain.ID;
import org.a1cey.bookshelf_pro_domain.Label;
import org.a1cey.bookshelf_pro_domain.book.Book;
import org.a1cey.bookshelf_pro_domain.book.BookRepository;
import org.a1cey.bookshelf_pro_domain.book.ISBN;
import org.a1cey.bookshelf_pro_domain.media_item.MediaItem;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

// TODO: implement this
public class jOOQBookRepository implements BookRepository {

    @Override
    public Book save(Book book) {
        return null;
    }

    @Override
    public Optional<Book> findByISBN(ISBN isbn) {
        return Optional.empty();
    }

    @Override
    public List<Book> findByAuthors(List<String> authors) {
        return List.of();
    }

    @Override
    public List<Book> findByPublishDate(LocalDate publishDate) {
        return List.of();
    }

    @Override
    public List<Book> findByPublisher(String publisher) {
        return List.of();
    }

    @Override
    public List<Book> findByPublishPlace(String publishPlace) {
        return List.of();
    }

    @Override
    public List<Book> findByPageCount(int pageCount) {
        return List.of();
    }

    @Override
    public void deleteByID(ID id) {

    }

    @Override
    public void deleteByISBN(ISBN isbn) {

    }

    @Override
    public Optional<MediaItem> findByID(ID id) {
        return Optional.empty();
    }

    @Override
    public List<MediaItem> findAll() {
        return List.of();
    }

    @Override
    public List<MediaItem> findByTitleContaining(String titleFragment) {
        return List.of();
    }

    @Override
    public List<MediaItem> findByLabels(List<Label> labels) {
        return List.of();
    }

}
