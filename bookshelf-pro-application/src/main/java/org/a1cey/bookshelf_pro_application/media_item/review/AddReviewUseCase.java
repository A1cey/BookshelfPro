package org.a1cey.bookshelf_pro_application.media_item.review;

import org.a1cey.bookshelf_pro_application.IdService;
import org.a1cey.bookshelf_pro_application.SecurityService;
import org.a1cey.bookshelf_pro_application.bookshelf.bookshelf_entry.AddBookshelfEntryUseCase;
import org.a1cey.bookshelf_pro_application.media_item.review.command.AddReviewCommand;
import org.a1cey.bookshelf_pro_domain.bookshelf.bookshelf_entry.BookshelfEntryRepository;
import org.a1cey.bookshelf_pro_domain.media_item.review.ReviewId;
import org.a1cey.bookshelf_pro_domain.media_item.review.ReviewRepository;
import org.a1cey.bookshelf_pro_domain.media_item.review.ReviewService;
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
                                 .orElseThrow(() -> new IllegalArgumentException("Media item not in bookshelf"));

        var id = idService.generateId();

        var review = reviewService.addReview(
            new ReviewId(id),
            command.mediaItemId(),
            command.accountId(),
            command.rating(),
            command.comment(),
            bookshelfEntry.consumptionProgress().snapshot()
        );

        reviewRepository.save(review, bookshelfEntry);
    }
}
