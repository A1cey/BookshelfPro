package org.a1cey.bookshelf_pro_domain;

import org.a1cey.bookshelf_pro_domain.account.AccountId;

public final class OwnershipPolicy {

    private OwnershipPolicy() {}

    public static void validate(AccountId owner, AccountId requestingUser, Id aggregateId) {
        if (!requestingUser.equals(owner)) {
            throw new IllegalStateException(
                "Change to aggregate " + aggregateId.value() + " requested by non-owner " + requestingUser
            );
        }
    }

}