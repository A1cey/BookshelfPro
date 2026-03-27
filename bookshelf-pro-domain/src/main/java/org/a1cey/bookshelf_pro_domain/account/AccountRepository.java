package org.a1cey.bookshelf_pro_domain.account;

import org.jmolecules.ddd.annotation.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository {

    Optional<Account> findById(AccountID id);

    void save(Account account);

    boolean existsUsername(Username name);

}
