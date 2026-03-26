package org.a1cey.bookshelf_pro_domain;

import org.a1cey.bookshelf_pro_domain.user.UserID;

public final class OwnershipPolicy {

    private OwnershipPolicy() {}

    public static void validate(UserID owner, UserID requestingUser, ID aggregateId) {
        if (!requestingUser.equals(owner)) {
            throw new IllegalStateException(
                    "Change to aggregate " + aggregateId + " requested by non-owner " + requestingUser
            );
        }
    }

}