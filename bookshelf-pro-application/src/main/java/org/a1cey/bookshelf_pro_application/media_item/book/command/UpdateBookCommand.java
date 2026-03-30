package org.a1cey.bookshelf_pro_application.media_item.book.command;

import java.net.URI;
import java.util.Optional;
import java.util.Set;

import org.a1cey.bookshelf_pro_domain.Title;
import org.a1cey.bookshelf_pro_domain.account.AccountId;
import org.a1cey.bookshelf_pro_domain.media_item.Description;
import org.a1cey.bookshelf_pro_domain.media_item.Language;
import org.a1cey.bookshelf_pro_domain.media_item.MediaItemId;
import org.a1cey.bookshelf_pro_domain.media_item.Subtitle;
import org.a1cey.bookshelf_pro_domain.media_item.book.Author;
import org.a1cey.bookshelf_pro_domain.media_item.book.PageCount;
import org.a1cey.bookshelf_pro_domain.media_item.book.PublishDate;
import org.a1cey.bookshelf_pro_domain.media_item.book.PublishPlace;
import org.a1cey.bookshelf_pro_domain.media_item.book.Publisher;

public record UpdateBookCommand(
    AccountId requestingUser,
    MediaItemId bookId,
    Optional<Title> title,
    Optional<Subtitle> subtitle,
    Optional<Description> description,
    Optional<URI> coverImageUrl,
    Optional<Set<Language>> languages,
    Optional<PageCount> pageCount,
    Optional<Set<Author>> authors,
    Optional<PublishDate> publishDate,
    Optional<Publisher> publisher,
    Optional<PublishPlace> publishPlace
) {}
