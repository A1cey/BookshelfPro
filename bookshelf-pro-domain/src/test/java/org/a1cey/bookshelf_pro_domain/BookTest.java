package org.a1cey.bookshelf_pro_domain;

import java.util.Set;
import java.util.UUID;

import org.a1cey.bookshelf_pro_domain.account.AccountId;
import org.a1cey.bookshelf_pro_domain.media_item.MediaItemId;
import org.a1cey.bookshelf_pro_domain.media_item.book.Author;
import org.a1cey.bookshelf_pro_domain.media_item.book.Book;
import org.a1cey.bookshelf_pro_domain.media_item.book.Isbn;
import org.a1cey.bookshelf_pro_domain.media_item.book.PageCount;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BookTest {

    private AccountId ownerId() {
        return new AccountId(UUID.randomUUID());
    }

    private Book.BookBuilder bookBuilder(AccountId owner) {
        return Book.builder(
            new MediaItemId(UUID.randomUUID()),
            owner,
            new Title("Clean Code"),
            new Isbn("9780132350884"),
            new PageCount(464)
        );
    }

    @Test
    void buildBookWithRequiredFields() {
        var owner = ownerId();
        var book = bookBuilder(owner).build();
        assertEquals(new Title("Clean Code"), book.title());
        assertEquals(464, book.pageCount().pageCount());
        assertEquals(owner, book.owner());
        assertEquals(new Isbn("9780132350884"), book.isbn());
    }

    @Test
    void buildBookWithAuthors() {
        var owner = ownerId();
        var book = bookBuilder(owner)
                       .authors(Set.of(new Author("Robert C. Martin")))
                       .build();
        assertEquals(1, book.authors().size());
        assertTrue(book.authors().stream().anyMatch(a -> a.name().equals("Robert C. Martin")));
    }

    @Test
    void changeTitle() {
        var owner = ownerId();
        var book = bookBuilder(owner).build();
        book.changeTitle(new Title("Clean Architecture"), owner);
        assertEquals("Clean Architecture", book.title().title());
    }

    @Test
    void changeTitleByNonOwnerThrows() {
        var owner = ownerId();
        var other = new AccountId(UUID.randomUUID());
        var book = bookBuilder(owner).build();
        assertThrows(SecurityException.class, () -> book.changeTitle(new Title("Other"), other));
    }

    @Test
    void negativePageCountegativeThrows() {
        assertThrows(IllegalArgumentException.class, () -> new PageCount(-1));
    }

    @Test
    void authorBlankNameThrows() {
        assertThrows(IllegalArgumentException.class, () -> new Author(""));
    }
}
