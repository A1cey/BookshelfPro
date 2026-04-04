package org.a1cey.bookshelf_pro_application.media_item.review.command;

import org.a1cey.bookshelf_pro_domain.account.AccountId;
import org.a1cey.bookshelf_pro_domain.account.Password;
import org.a1cey.bookshelf_pro_domain.account.Username;
import org.a1cey.bookshelf_pro_domain.media_item.review.ReviewId;

public record DeleteReviewCommand(ReviewId reviewId, AccountId owner, Username username, Password password) {}
