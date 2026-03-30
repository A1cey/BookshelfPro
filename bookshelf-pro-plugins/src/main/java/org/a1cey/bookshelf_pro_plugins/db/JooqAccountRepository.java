package org.a1cey.bookshelf_pro_plugins.db;

import java.util.Optional;

import org.a1cey.bookshelf_pro_domain.account.Account;
import org.a1cey.bookshelf_pro_domain.account.AccountId;
import org.a1cey.bookshelf_pro_domain.account.AccountRepository;
import org.a1cey.bookshelf_pro_domain.account.AccountService;
import org.a1cey.bookshelf_pro_domain.account.Email;
import org.a1cey.bookshelf_pro_domain.account.Password;
import org.a1cey.bookshelf_pro_domain.account.Username;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import static org.a1cey.bookshelf_pro_plugins.db.jooq.Tables.ACCOUNT;

@Repository
public class JooqAccountRepository implements AccountRepository {
    private final DSLContext dsl;

    public JooqAccountRepository(DSLContext dslContext) {
        this.dsl = dslContext;
    }

    @Override
    public Optional<Account> findById(AccountId id) {
        var accountRecord = dsl.fetchOne(ACCOUNT, ACCOUNT.ID.eq(id.value()));

        if (accountRecord == null) {
            return Optional.empty();
        }

        AccountService accountService = new AccountService(this);

        var account = accountService.buildAccount(
            id,
            new Username(accountRecord.getUsername()),
            new Email(accountRecord.getEmail()),
            new Password(accountRecord.getPassword())
        );

        return Optional.of(account);
    }

    @Override
    public void save(Account account) {
        // TODO:
    }

    @Override
    public boolean existsUsername(Username name) {
        return dsl.fetchExists(ACCOUNT, ACCOUNT.USERNAME.eq(name.name()));
    }
}
