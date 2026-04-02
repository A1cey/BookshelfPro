package org.a1cey.bookshelf_pro_application.bookshelf;

import java.util.stream.Collectors;

import org.a1cey.bookshelf_pro_application.SecurityService;
import org.a1cey.bookshelf_pro_application.bookshelf.command.GetAllBookshelfEntriesCommand;
import org.a1cey.bookshelf_pro_application.bookshelf.result.GetAllBookshelfEntriesResult;
import org.a1cey.bookshelf_pro_application.dto.BookshelfEntryDto;
import org.a1cey.bookshelf_pro_domain.bookshelf.bookshelf_entry.BookshelfEntryRepository;
import org.springframework.stereotype.Service;

@Service
public class GetAllBookshelfEntriesUseCase {

    private final SecurityService securityService;
    private final BookshelfEntryRepository bookshelfEntryRepository;

    public GetAllBookshelfEntriesUseCase(SecurityService securityService, BookshelfEntryRepository bookshelfEntryRepository) {
        this.securityService = securityService;
        this.bookshelfEntryRepository = bookshelfEntryRepository;
    }

    public GetAllBookshelfEntriesResult execute(GetAllBookshelfEntriesCommand command) {
        var owner = securityService.checkUser(command.accountId(), command.name(), command.password());
        var entries = bookshelfEntryRepository
                          .findByAccount(owner.id())
                          .stream()
                          .map(BookshelfEntryDto::from)
                          .collect(Collectors.toSet());
        return new GetAllBookshelfEntriesResult(entries);
    }
}
