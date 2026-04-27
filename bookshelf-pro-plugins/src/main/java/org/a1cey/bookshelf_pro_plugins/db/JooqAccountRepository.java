package org.a1cey.bookshelf_pro_plugins.db;

import java.util.Optional;

import org.a1cey.bookshelf_pro_domain.account.Account;
import org.a1cey.bookshelf_pro_domain.account.AccountId;
import org.a1cey.bookshelf_pro_domain.account.AccountRepository;
import org.a1cey.bookshelf_pro_domain.account.Email;
import org.a1cey.bookshelf_pro_domain.account.Password;
import org.a1cey.bookshelf_pro_domain.account.Username;
import org.a1cey.bookshelf_pro_plugins.db.jooq.tables.records.AccountRecord;
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
        return dsl.fetchOptional(ACCOUNT, ACCOUNT.ID.eq(id.value())).map(this::mapRecord);
    }

    @Override
    public Optional<Account> findByUsername(Username username) {
        return dsl.fetchOptional(ACCOUNT, ACCOUNT.USERNAME.eq(username.name())).map(this::mapRecord);
    }

    private Account mapRecord(AccountRecord record) {
        var emailStr = record.getEmail();

        Email email = null;
        if (emailStr != null) {
            email = new Email(emailStr);
        }

        var deleted = record.getDeleted();
        if (deleted == null) {
            deleted = false;
        }

        return new Account(
            new AccountId(record.getId()),
            new Username(record.getUsername()),
            email,
            new Password(record.getPassword()),
            deleted
        );
    }

    @Override
    public void save(Account account) {
        dsl.insertInto(ACCOUNT)
           .set(ACCOUNT.ID, account.id().value())
           .set(ACCOUNT.USERNAME, account.name().name())
           .set(ACCOUNT.PASSWORD, account.password().hashedPassword())
           .set(ACCOUNT.EMAIL, account.email().map(Email::email).orElse(null))
           .set(ACCOUNT.DELETED, account.isDeleted())
           .execute();
    }

    @Override
    public void update(Account account) {
        dsl.update(ACCOUNT)
           .set(ACCOUNT.USERNAME, account.name().name())
           .set(ACCOUNT.PASSWORD, account.password().hashedPassword())
           .set(ACCOUNT.EMAIL, account.email().map(Email::email).orElse(null))
           .set(ACCOUNT.DELETED, account.isDeleted())
           .where(ACCOUNT.ID.eq(account.id().value()))
           .execute();
    }

    @Override
    public void delete(AccountId id) {
        dsl.update(ACCOUNT)
           .set(ACCOUNT.DELETED, true)
           .where(ACCOUNT.ID.eq(id.value()))
           .execute();
    }

    @Override
    public boolean existsUsername(Username name) {
        return dsl.fetchExists(ACCOUNT, ACCOUNT.USERNAME.eq(name.name()));
    }
}
