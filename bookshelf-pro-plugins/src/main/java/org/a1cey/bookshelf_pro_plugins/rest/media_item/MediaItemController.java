package org.a1cey.bookshelf_pro_plugins.rest.media_item;

import java.util.UUID;

import org.a1cey.bookshelf_pro_application.media_item.GetAllMediaItemsUseCase;
import org.a1cey.bookshelf_pro_application.media_item.result.GetAllMediaItemsResult;
import org.a1cey.bookshelf_pro_application.review.AddReviewUseCase;
import org.a1cey.bookshelf_pro_application.review.command.AddReviewCommand;
import org.a1cey.bookshelf_pro_domain.account.AccountId;
import org.a1cey.bookshelf_pro_domain.account.Password;
import org.a1cey.bookshelf_pro_domain.account.Username;
import org.a1cey.bookshelf_pro_domain.media_item.MediaItemId;
import org.a1cey.bookshelf_pro_domain.review.Comment;
import org.a1cey.bookshelf_pro_domain.review.Rating;
import org.a1cey.bookshelf_pro_plugins.rest.media_item.request.ReviewRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/media-item")
public class MediaItemController {
    private final GetAllMediaItemsUseCase getAllMediaItemsUseCase;
    private final AddReviewUseCase addReviewUseCase;

    public MediaItemController(GetAllMediaItemsUseCase getAllMediaItemsUseCase, AddReviewUseCase addReviewUseCase) {
        this.getAllMediaItemsUseCase = getAllMediaItemsUseCase;
        this.addReviewUseCase = addReviewUseCase;
    }

    @GetMapping()
    public ResponseEntity<GetAllMediaItemsResult> getAllBooks() {
        return ResponseEntity.ok(getAllMediaItemsUseCase.execute());
    }

    @PostMapping("/{mediaItemId}/review")
    public void review(@PathVariable UUID mediaItemId, @RequestBody ReviewRequest request) {
        addReviewUseCase.execute(new AddReviewCommand(
            new AccountId(request.accountId()),
            new Username(request.name()),
            new Password(request.password()),
            new MediaItemId(mediaItemId),
            new Rating(request.rating()),
            new Comment(request.comment())
        ));
    }
}