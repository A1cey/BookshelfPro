package org.a1cey.bookshelf_pro_domain;

import org.a1cey.bookshelf_pro_domain.account.AccountID;

public final class OwnershipPolicy {

    private OwnershipPolicy() {}

    public static void validate(AccountID owner, AccountID requestingUser, ID aggregateId) {
        if (!requestingUser.equals(owner)) {
            throw new IllegalStateException(
                    "Change to aggregate " + aggregateId.value() + " requested by non-owner " + requestingUser
            );
        }
    }

}