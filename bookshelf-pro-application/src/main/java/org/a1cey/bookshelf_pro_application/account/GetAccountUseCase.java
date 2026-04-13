package org.a1cey.bookshelf_pro_application.account;

import java.util.stream.Collectors;

import org.a1cey.bookshelf_pro_application.account.result.GetAccountResult;
import org.a1cey.bookshelf_pro_application.dto.BookDto;
import org.a1cey.bookshelf_pro_application.dto.ReviewDto;
import org.a1cey.bookshelf_pro_application.security.CurrentUserProvider;
import org.a1cey.bookshelf_pro_domain.media_item.MediaItemRepository;
import org.a1cey.bookshelf_pro_domain.media_item.MediaItemType;
import org.a1cey.bookshelf_pro_domain.media_item.book.Book;
import org.a1cey.bookshelf_pro_domain.media_item.review.ReviewRepository;
import org.springframework.stereotype.Service;

@Service
public final class GetAccountUseCase {
    private final ReviewRepository reviewRepository;
    private final MediaItemRepository mediaItemRepository;
    private final CurrentUserProvider currentUserProvider;

    public GetAccountUseCase(
        ReviewRepository reviewRepository,
        MediaItemRepository mediaItemRepository,
        CurrentUserProvider currentUserProvider
    ) {
        this.reviewRepository = reviewRepository;
        this.mediaItemRepository = mediaItemRepository;
        this.currentUserProvider = currentUserProvider;
    }

    public GetAccountResult execute() {
        var account = currentUserProvider.currentUser();
        var reviews = reviewRepository.findByOwner(account.id()).stream().map(ReviewDto::from).collect(Collectors.toSet());
        var mediaItems = mediaItemRepository.findByOwner(account.id()).stream().map(
            mediaItem -> switch (mediaItem.type()) {
                case MediaItemType.BOOK -> BookDto.from((Book) mediaItem);
            }).collect(Collectors.toSet());

        return new GetAccountResult(account.id(), account.name(), account.email(), reviews, mediaItems);
    }
}
