package org.a1cey.bookshelf_pro_application.account;

import org.a1cey.bookshelf_pro_application.SecurityService;
import org.a1cey.bookshelf_pro_application.account.command.GetAccountCommand;
import org.a1cey.bookshelf_pro_application.account.result.GetAccountResult;
import org.a1cey.bookshelf_pro_domain.media_item.MediaItemRepository;
import org.a1cey.bookshelf_pro_domain.media_item.review.ReviewRepository;
import org.springframework.stereotype.Service;

@Service
public final class GetAccountUseCase {
    private final SecurityService securityService;
    private final ReviewRepository reviewRepository;
    private final MediaItemRepository mediaItemRepository;

    public GetAccountUseCase(SecurityService securityService, ReviewRepository reviewRepository, MediaItemRepository mediaItemRepository) {
        this.securityService = securityService;
        this.reviewRepository = reviewRepository;
        this.mediaItemRepository = mediaItemRepository;
    }

    public GetAccountResult execute(GetAccountCommand command) {
        var account = securityService.checkUser(command.accountId(), command.name(), command.password());
        var reviews = reviewRepository.findByOwner(account.id());
        var mediaItems = mediaItemRepository.findByOwner(account.id());
        return new GetAccountResult(account.id(), account.name(), account.email(), reviews, mediaItems);
    }
}
