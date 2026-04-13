package org.a1cey.bookshelf_pro_application.media_item.review;

import org.a1cey.bookshelf_pro_application.IdService;
import org.a1cey.bookshelf_pro_application.media_item.review.command.AddReviewCommand;
import org.a1cey.bookshelf_pro_application.security.CurrentUserProvider;
import org.a1cey.bookshelf_pro_domain.bookshelf.bookshelf_entry.BookshelfEntryRepository;
import org.a1cey.bookshelf_pro_domain.media_item.review.ReviewId;
import org.a1cey.bookshelf_pro_domain.media_item.review.ReviewRepository;
import org.a1cey.bookshelf_pro_domain.media_item.review.ReviewService;
import org.springframework.stereotype.Service;

@Service
public class AddReviewUseCase {
    private final ReviewService reviewService;
    private final IdService idService;
    private final BookshelfEntryRepository bookshelfEntryRepository;
    private final ReviewRepository reviewRepository;
    private final CurrentUserProvider currentUserProvider;

    public AddReviewUseCase(
        ReviewService reviewService,
        IdService idService,
        BookshelfEntryRepository bookshelfEntryRepository,
        ReviewRepository reviewRepository,
        CurrentUserProvider currentUserProvider
    ) {
        this.reviewService = reviewService;
        this.idService = idService;
        this.bookshelfEntryRepository = bookshelfEntryRepository;
        this.reviewRepository = reviewRepository;
        this.currentUserProvider = currentUserProvider;
    }

    public void execute(AddReviewCommand command) {
        var owner = currentUserProvider.currentUser();
        var bookshelfEntry = bookshelfEntryRepository
                                 .findByAccountAndMediaItem(owner.id(), command.mediaItemId())
                                 .orElseThrow(() -> new IllegalArgumentException("Media item not in bookshelf"));

        var id = idService.generateId();

        var review = reviewService.addReview(
            new ReviewId(id),
            command.mediaItemId(),
            owner.id(),
            command.rating(),
            command.comment(),
            bookshelfEntry.consumptionProgress().snapshot()
        );

        reviewRepository.save(review, bookshelfEntry);
    }
}
