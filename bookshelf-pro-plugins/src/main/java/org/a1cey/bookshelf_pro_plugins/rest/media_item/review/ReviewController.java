package org.a1cey.bookshelf_pro_plugins.rest.media_item.review;

import java.util.Optional;
import java.util.UUID;

import org.a1cey.bookshelf_pro_application.media_item.review.AddReviewUseCase;
import org.a1cey.bookshelf_pro_application.media_item.review.UpdateReviewUseCase;
import org.a1cey.bookshelf_pro_application.media_item.review.command.AddReviewCommand;
import org.a1cey.bookshelf_pro_application.media_item.review.command.UpdateReviewCommand;
import org.a1cey.bookshelf_pro_domain.account.AccountId;
import org.a1cey.bookshelf_pro_domain.account.Password;
import org.a1cey.bookshelf_pro_domain.account.Username;
import org.a1cey.bookshelf_pro_domain.media_item.MediaItemId;
import org.a1cey.bookshelf_pro_domain.media_item.review.Comment;
import org.a1cey.bookshelf_pro_domain.media_item.review.Rating;
import org.a1cey.bookshelf_pro_domain.media_item.review.ReviewId;
import org.a1cey.bookshelf_pro_plugins.rest.Credentials;
import org.a1cey.bookshelf_pro_plugins.rest.media_item.request.ReviewRequest;
import org.a1cey.bookshelf_pro_plugins.rest.media_item.request.UpdateReviewRequest;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// TODO: How to structure review endpoint?

@RestController
@RequestMapping("/media-item/{mediaItemId}/review")
public class ReviewController {
    private final AddReviewUseCase addReviewUseCase;
    private final UpdateReviewUseCase updateReviewUseCase;

    public ReviewController(AddReviewUseCase addReviewUseCase, UpdateReviewUseCase updateReviewUseCase) {
        this.addReviewUseCase = addReviewUseCase;
        this.updateReviewUseCase = updateReviewUseCase;
    }

    @PostMapping
    public void review(@PathVariable UUID mediaItemId, @ParameterObject Credentials credentials, @RequestBody ReviewRequest request) {
        addReviewUseCase.execute(new AddReviewCommand(
            new AccountId(credentials.accountId()),
            new Username(credentials.username()),
            new Password(credentials.password()),
            new MediaItemId(mediaItemId),
            new Rating(request.rating()),
            new Comment(request.comment())
        ));
    }

    @PatchMapping("/{reviewId}")
    public void update(
        @PathVariable UUID reviewId,
        @ParameterObject Credentials credentials,
        @RequestBody UpdateReviewRequest request
    ) {
        updateReviewUseCase.execute(new UpdateReviewCommand(
            new AccountId(credentials.accountId()),
            new Username(credentials.username()),
            new Password(credentials.password()),
            new ReviewId(reviewId),
            Optional.ofNullable(request.rating()).map(Rating::new),
            Optional.ofNullable(request.comment()).map(Comment::new)
        ));
    }
}