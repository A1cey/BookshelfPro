package org.a1cey.bookshelf_pro_domain.account;

import java.util.Optional;

import org.jmolecules.ddd.annotation.Repository;

@Repository
public interface AccountRepository {

    Optional<Account> findById(AccountId id);

    void save(Account account);

    void update(Account account);

    void delete(AccountId id);

    boolean existsUsername(Username name);

}
