package org.a1cey.bookshelf_pro_application.media_item.review.command;

import org.a1cey.bookshelf_pro_domain.account.AccountId;
import org.a1cey.bookshelf_pro_domain.account.Password;
import org.a1cey.bookshelf_pro_domain.account.Username;
import org.a1cey.bookshelf_pro_domain.media_item.review.ReviewId;

<<<<<<< HEAD
public record DeleteReviewCommand(
    AccountId owner,
    Username name,
    Password password,
    ReviewId reviewId
) {}
=======
public record DeleteReviewCommand(ReviewId reviewId, AccountId owner, Username username, Password password) {}
>>>>>>> 8e445b1acaa87311be1e7031008f5be9a8fb53ed
