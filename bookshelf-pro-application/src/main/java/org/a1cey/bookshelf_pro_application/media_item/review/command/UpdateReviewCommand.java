package org.a1cey.bookshelf_pro_application.media_item.review.command;

import java.util.Optional;

import org.a1cey.bookshelf_pro_domain.account.AccountId;
import org.a1cey.bookshelf_pro_domain.account.Password;
import org.a1cey.bookshelf_pro_domain.account.Username;
import org.a1cey.bookshelf_pro_domain.media_item.review.Comment;
import org.a1cey.bookshelf_pro_domain.media_item.review.Rating;
import org.a1cey.bookshelf_pro_domain.media_item.review.ReviewId;

public record UpdateReviewCommand(
    AccountId accountId,
    Username name,
    Password password,
    ReviewId reviewId,
    Optional<Rating> newRating,
    Optional<Comment> newComment
) {}
