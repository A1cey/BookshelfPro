package org.a1cey.bookshelf_pro_application.security;

import org.a1cey.bookshelf_pro_domain.account.Account;

public interface CurrentUserProvider {
    Account currentUser();
}
