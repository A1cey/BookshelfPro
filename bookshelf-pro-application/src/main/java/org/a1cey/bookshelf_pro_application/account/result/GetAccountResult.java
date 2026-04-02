package org.a1cey.bookshelf_pro_application.account.result;

import java.util.Optional;
import java.util.Set;

import org.a1cey.bookshelf_pro_domain.account.AccountId;
import org.a1cey.bookshelf_pro_domain.account.Email;
import org.a1cey.bookshelf_pro_domain.account.Username;
import org.a1cey.bookshelf_pro_domain.media_item.MediaItem;
import org.a1cey.bookshelf_pro_domain.media_item.review.Review;

public record GetAccountResult(
    AccountId id,
    Username name,
    Optional<Email> email,
    Set<Review> reviews,
    Set<? extends MediaItem> mediaItems
) {}

