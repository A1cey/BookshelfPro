package org.a1cey.bookshelf_pro_application.review;

import java.util.Set;

import org.a1cey.bookshelf_pro_application.IdService;
import org.a1cey.bookshelf_pro_application.SecurityService;
import org.a1cey.bookshelf_pro_application.bookshelf_entry.AddBookshelfEntryUseCase;
import org.a1cey.bookshelf_pro_application.bookshelf_entry.command.AddBookshelfEntryCommand;
import org.a1cey.bookshelf_pro_application.review.command.AddReviewCommand;
import org.a1cey.bookshelf_pro_domain.bookshelf_entry.BookshelfEntryRepository;
import org.a1cey.bookshelf_pro_domain.review.ReviewId;
import org.a1cey.bookshelf_pro_domain.review.ReviewRepository;
import org.a1cey.bookshelf_pro_domain.review.ReviewService;
import org.springframework.stereotype.Service;

@Service
public class AddReviewUseCase {
    private final SecurityService securityService;
    private final ReviewService reviewService;
    private final IdService idService;
    private final BookshelfEntryRepository bookshelfEntryRepository;
    private final AddBookshelfEntryUseCase addBookshelfEntryUseCase;
    private final ReviewRepository reviewRepository;

    public AddReviewUseCase(
        SecurityService securityService,
        ReviewService reviewService,
        IdService idService,
        BookshelfEntryRepository bookshelfEntryRepository,
        AddBookshelfEntryUseCase addBookshelfEntryUseCase,
        ReviewRepository reviewRepository
    ) {
        this.securityService = securityService;
        this.reviewService = reviewService;
        this.idService = idService;
        this.bookshelfEntryRepository = bookshelfEntryRepository;
        this.addBookshelfEntryUseCase = addBookshelfEntryUseCase;
        this.reviewRepository = reviewRepository;
    }

    public void execute(AddReviewCommand command) {
        var account = securityService.checkUser(command.accountId(), command.name(), command.password());

        var bookshelfEntry = bookshelfEntryRepository
                                 .findByAccountAndMediaItem(account.id(), command.mediaItemId())
                                 .orElseGet(() -> addBookshelfEntryUseCase.execute(
                                     new AddBookshelfEntryCommand(
                                         command.accountId(),
                                         command.name(),
                                         command.password(),
                                         command.mediaItemId(),
                                         Set.of()
                                     )
                                 ));

        var id = idService.generateId();

        var review = reviewService.addReview(
            new ReviewId(id),
            command.mediaItemId(),
            command.accountId(),
            command.rating(),
            command.comment(),
            bookshelfEntry.consumptionProgress().snapshot()
        );

        reviewRepository.save(review);
    }
}
