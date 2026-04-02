package org.a1cey.bookshelf_pro_application.media_item.review.command;

import org.a1cey.bookshelf_pro_domain.account.AccountId;
import org.a1cey.bookshelf_pro_domain.account.Password;
import org.a1cey.bookshelf_pro_domain.account.Username;
import org.a1cey.bookshelf_pro_domain.media_item.MediaItemId;
import org.a1cey.bookshelf_pro_domain.media_item.review.Comment;
import org.a1cey.bookshelf_pro_domain.media_item.review.Rating;

public record AddReviewCommand(
    AccountId accountId,
    Username name,
    Password password,
    MediaItemId mediaItemId,
    Rating rating,
    Comment comment
) {}
