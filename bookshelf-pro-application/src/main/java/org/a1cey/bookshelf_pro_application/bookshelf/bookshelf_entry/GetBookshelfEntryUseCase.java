package org.a1cey.bookshelf_pro_application.bookshelf.bookshelf_entry;

import java.util.Optional;

import org.a1cey.bookshelf_pro_application.SecurityService;
import org.a1cey.bookshelf_pro_application.bookshelf.bookshelf_entry.command.GetBookshelfEntryCommand;
import org.a1cey.bookshelf_pro_application.bookshelf.bookshelf_entry.result.GetBookshelfEntryResult;
import org.a1cey.bookshelf_pro_application.dto.BookshelfEntryDto;
import org.a1cey.bookshelf_pro_domain.bookshelf.bookshelf_entry.BookshelfEntryRepository;
import org.springframework.stereotype.Service;

@Service
public class GetBookshelfEntryUseCase {

    private final SecurityService securityService;
    private final BookshelfEntryRepository bookshelfEntryRepository;

    public GetBookshelfEntryUseCase(SecurityService securityService, BookshelfEntryRepository bookshelfEntryRepository) {
        this.securityService = securityService;
        this.bookshelfEntryRepository = bookshelfEntryRepository;
    }

    public Optional<GetBookshelfEntryResult> execute(GetBookshelfEntryCommand command) {
        var owner = securityService.checkUser(command.accountId(), command.name(), command.password());
        return bookshelfEntryRepository
                   .findById(command.bookshelfEntryId())
                   .map(entry -> {
                       if (!entry.owner().equals(owner.id())) {
                           throw new SecurityException(
                               "This user does not have access to the bookshelf entry " + "with the id." + entry.id()
                           );
                       }
                       return new GetBookshelfEntryResult(BookshelfEntryDto.from(entry));
                   });
    }
}
