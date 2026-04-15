package org.a1cey.bookshelf_pro_application;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.a1cey.bookshelf_pro_application.bookshelf.bookshelf_entry.AddBookshelfEntryUseCase;
import org.a1cey.bookshelf_pro_application.media_item.review.AddReviewUseCase;
import org.a1cey.bookshelf_pro_application.media_item.review.DeleteReviewUseCase;
import org.a1cey.bookshelf_pro_application.media_item.review.UpdateReviewUseCase;
import org.a1cey.bookshelf_pro_application.media_item.review.command.AddReviewCommand;
import org.a1cey.bookshelf_pro_application.media_item.review.command.DeleteReviewCommand;
import org.a1cey.bookshelf_pro_application.media_item.review.command.UpdateReviewCommand;
import org.a1cey.bookshelf_pro_application.security.CurrentUserProvider;
import org.a1cey.bookshelf_pro_domain.account.Account;
import org.a1cey.bookshelf_pro_domain.account.AccountId;
import org.a1cey.bookshelf_pro_domain.account.Password;
import org.a1cey.bookshelf_pro_domain.account.Username;
import org.a1cey.bookshelf_pro_domain.bookshelf.bookshelf_entry.BookshelfEntry;
import org.a1cey.bookshelf_pro_domain.bookshelf.bookshelf_entry.BookshelfEntryId;
import org.a1cey.bookshelf_pro_domain.bookshelf.bookshelf_entry.BookshelfEntryRepository;
import org.a1cey.bookshelf_pro_domain.bookshelf.bookshelf_entry.consumption.ConsumptionProgress;
import org.a1cey.bookshelf_pro_domain.bookshelf.bookshelf_entry.consumption.ConsumptionProgressId;
import org.a1cey.bookshelf_pro_domain.bookshelf.bookshelf_entry.consumption.ConsumptionProgressSnapshot;
import org.a1cey.bookshelf_pro_domain.bookshelf.bookshelf_entry.consumption.ConsumptionState;
import org.a1cey.bookshelf_pro_domain.media_item.MediaItemId;
import org.a1cey.bookshelf_pro_domain.media_item.book.BookConsumptionProgress;
import org.a1cey.bookshelf_pro_domain.media_item.book.PageCount;
import org.a1cey.bookshelf_pro_domain.media_item.review.Comment;
import org.a1cey.bookshelf_pro_domain.media_item.review.Rating;
import org.a1cey.bookshelf_pro_domain.media_item.review.Review;
import org.a1cey.bookshelf_pro_domain.media_item.review.ReviewChange;
import org.a1cey.bookshelf_pro_domain.media_item.review.ReviewId;
import org.a1cey.bookshelf_pro_domain.media_item.review.ReviewRepository;
import org.a1cey.bookshelf_pro_domain.media_item.review.ReviewService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReviewUseCaseTest {

    @Mock
    private ReviewRepository reviewRepository;
    @Mock
    private ReviewService reviewService;
    @Mock
    private IdService idService;
    @Mock
    private BookshelfEntryRepository bookshelfEntryRepository;
    @Mock
    private AddBookshelfEntryUseCase addBookshelfEntryUseCase;
    @Mock
    private CurrentUserProvider currentUserProvider;

    @InjectMocks
    private AddReviewUseCase addReviewUseCase;
    @InjectMocks
    private DeleteReviewUseCase deleteReviewUseCase;
    @InjectMocks
    private UpdateReviewUseCase updateReviewUseCase;

    private final AccountId ownerId = new AccountId(UUID.randomUUID());
    private final Username username = new Username("alice");
    private final Password password = new Password("hash");
    private final Account account = new Account(ownerId, username, null, password);
    private final MediaItemId mediaItemId = new MediaItemId(UUID.randomUUID());

    private BookshelfEntry stubEntry() {
        var progress = BookConsumptionProgress.reconstruct(new PageCount(50), new PageCount(200));
        var cp = new ConsumptionProgress(new ConsumptionProgressId(UUID.randomUUID()), progress, ConsumptionState.STARTED);
        return BookshelfEntry.builder(
            new BookshelfEntryId(UUID.randomUUID()), mediaItemId, ownerId, cp
        ).build();
    }

    private Review stubReview(AccountId owner) {
        var change = new ReviewChange(
            new Rating(7f), new Comment("ok"),
            new ConsumptionProgressSnapshot(
                ConsumptionState.STARTED,
                BookConsumptionProgress.reconstruct(new PageCount(50), new PageCount(200))
            )
        );
        return Review.reconstruct(new ReviewId(UUID.randomUUID()), mediaItemId, owner, List.of(change));
    }

    @Test
    void addReviewExistingEntry() {
        var entry = stubEntry();
        var review = stubReview(ownerId);
        when(currentUserProvider.currentUser()).thenReturn(account);
        when(bookshelfEntryRepository.findByAccountAndMediaItem(ownerId, mediaItemId)).thenReturn(Optional.of(entry));
        when(idService.generateId()).thenReturn(UUID.randomUUID());
        when(reviewService.addReview(any(), any(), any(), any(), any(), any())).thenReturn(review);

        addReviewUseCase.execute(new AddReviewCommand(mediaItemId, new Rating(7f), new Comment("ok")));

        verify(reviewRepository).save(review, entry);
    }

    @Test
    void deleteReview() {
        var review = stubReview(ownerId);
        when(currentUserProvider.currentUser()).thenReturn(account);
        when(reviewRepository.findById(review.id())).thenReturn(Optional.of(review));

        deleteReviewUseCase.execute(new DeleteReviewCommand(review.id()));

        verify(reviewRepository).delete(review.id());
    }

    @Test
    void deleteReviewByNonOwnerThrows() {
        var otherOwner = new AccountId(UUID.randomUUID());
        var review = stubReview(otherOwner);
        when(currentUserProvider.currentUser()).thenReturn(account);
        when(reviewRepository.findById(review.id())).thenReturn(Optional.of(review));

        assertThrows(SecurityException.class, () -> deleteReviewUseCase.execute(new DeleteReviewCommand(review.id())));
        verify(reviewRepository, never()).delete(any());
    }

    @Test
    void updateReviewWithNewRating() {
        var review = spy(stubReview(ownerId));
        var entry = stubEntry();
        when(currentUserProvider.currentUser()).thenReturn(account);
        when(reviewRepository.findById(review.id())).thenReturn(Optional.of(review));
        when(bookshelfEntryRepository.findByAccountAndMediaItem(ownerId, mediaItemId)).thenReturn(Optional.of(entry));

        updateReviewUseCase.execute(new UpdateReviewCommand(review.id(), Optional.of(new Rating(9f)), Optional.empty()));

        verify(review).changeRating(eq(new Rating(9f)), any(), eq(ownerId));
        verify(reviewRepository).update(review, entry);
    }

    @Test
    void updateReviewWithNewComment() {
        var review = spy(stubReview(ownerId));
        var entry = stubEntry();
        when(currentUserProvider.currentUser()).thenReturn(account);

        when(reviewRepository.findById(review.id())).thenReturn(Optional.of(review));
        when(bookshelfEntryRepository.findByAccountAndMediaItem(ownerId, mediaItemId)).thenReturn(Optional.of(entry));

        updateReviewUseCase.execute(new UpdateReviewCommand(review.id(), Optional.empty(), Optional.of(new Comment("updated"))));

        verify(review).changeComment(eq(new Comment("updated")), any(), eq(ownerId));
    }
}
