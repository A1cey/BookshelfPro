package org.a1cey.bookshelf_pro_application.bookshelf.bookshelf_entry;

import java.util.stream.Collectors;

import org.a1cey.bookshelf_pro_application.bookshelf.bookshelf_entry.result.GetAllBookshelfEntriesResult;
import org.a1cey.bookshelf_pro_application.dto.BookshelfEntryDto;
import org.a1cey.bookshelf_pro_application.security.CurrentUserProvider;
import org.a1cey.bookshelf_pro_domain.bookshelf.bookshelf_entry.BookshelfEntryRepository;
import org.springframework.stereotype.Service;

@Service
public class GetAllBookshelfEntriesUseCase {

    private final BookshelfEntryRepository bookshelfEntryRepository;
    private final CurrentUserProvider currentUserProvider;

    public GetAllBookshelfEntriesUseCase(BookshelfEntryRepository bookshelfEntryRepository, CurrentUserProvider currentUserProvider) {
        this.bookshelfEntryRepository = bookshelfEntryRepository;
        this.currentUserProvider = currentUserProvider;
    }

    public GetAllBookshelfEntriesResult execute() {
        var owner = currentUserProvider.currentUser();
        var entries = bookshelfEntryRepository
                          .findByAccount(owner.id())
                          .stream()
                          .map(BookshelfEntryDto::from)
                          .collect(Collectors.toSet());
        return new GetAllBookshelfEntriesResult(entries);
    }
}
