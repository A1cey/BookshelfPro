package org.a1cey.bookshelf_pro_application.bookshelf.bookshelf_entry;

import java.util.NoSuchElementException;

import org.a1cey.bookshelf_pro_application.SecurityService;
import org.a1cey.bookshelf_pro_application.bookshelf.bookshelf_entry.command.UpdateBookshelfEntryCommand;
import org.a1cey.bookshelf_pro_domain.bookshelf.bookshelf_entry.BookshelfEntryRepository;
import org.springframework.stereotype.Service;

@Service
public class UpdateBookshelfEntryUseCase {

    private final SecurityService securityService;
    private final BookshelfEntryRepository bookshelfEntryRepository;

    public UpdateBookshelfEntryUseCase(SecurityService securityService, BookshelfEntryRepository bookshelfEntryRepository) {
        this.securityService = securityService;
        this.bookshelfEntryRepository = bookshelfEntryRepository;
    }

    public void execute(UpdateBookshelfEntryCommand command) {
        var user = securityService.checkUser(command.accountId(), command.name(), command.password());

        var bookshelfEntry = bookshelfEntryRepository
                                 .findById(command.bookshelfEntryId())
                                 .orElseThrow(() -> new NoSuchElementException(
                                     "Book entry with id " + command.bookshelfEntryId() + " not found")
                                 );

        if (!bookshelfEntry.owner().equals(user.id())) {
            throw new SecurityException("User not allowed to perform this action as they do not own the bookshelf entry");
        }

        command.consumptionProgressNumber().ifPresent(consumptionProgressNumber -> {
            var newConsumptionProgress = bookshelfEntry.consumptionProgress().progress().update(consumptionProgressNumber);
            bookshelfEntry.updateConsumptionProgress(newConsumptionProgress, user.id());
        });
        command.labels().ifPresent(labels -> bookshelfEntry.changeLabels(labels, user.id()));

        bookshelfEntryRepository.update(bookshelfEntry);
    }
}
