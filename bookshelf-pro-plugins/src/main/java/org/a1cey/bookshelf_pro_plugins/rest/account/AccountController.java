package org.a1cey.bookshelf_pro_plugins.rest.account;

import java.util.Optional;
import java.util.UUID;

import org.a1cey.bookshelf_pro_application.account.CreateAccountUseCase;
import org.a1cey.bookshelf_pro_application.account.DeleteAccountUseCase;
import org.a1cey.bookshelf_pro_application.account.GetAccountUseCase;
import org.a1cey.bookshelf_pro_application.account.UpdateAccountUseCase;
import org.a1cey.bookshelf_pro_application.account.command.CreateAccountCommand;
import org.a1cey.bookshelf_pro_application.account.command.DeleteAccountCommand;
import org.a1cey.bookshelf_pro_application.account.command.GetAccountCommand;
import org.a1cey.bookshelf_pro_application.account.command.UpdateAccountCommand;
import org.a1cey.bookshelf_pro_application.account.result.CreateAccountResult;
import org.a1cey.bookshelf_pro_application.account.result.GetAccountResult;
import org.a1cey.bookshelf_pro_domain.account.AccountId;
import org.a1cey.bookshelf_pro_domain.account.Email;
import org.a1cey.bookshelf_pro_domain.account.Password;
import org.a1cey.bookshelf_pro_domain.account.Username;
import org.a1cey.bookshelf_pro_plugins.rest.account.request.CreateAccountRequest;
import org.a1cey.bookshelf_pro_plugins.rest.account.request.DeleteAccountRequest;
import org.a1cey.bookshelf_pro_plugins.rest.account.request.GetAccountRequest;
import org.a1cey.bookshelf_pro_plugins.rest.account.request.UpdateAccountRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/account")
public class AccountController {
    private final CreateAccountUseCase createAccountUseCase;
    private final DeleteAccountUseCase deleteAccountUseCase;
    private final UpdateAccountUseCase updateAccountUseCase;
    private final GetAccountUseCase getAccountUseCase;

    public AccountController(CreateAccountUseCase createAccountUseCase, DeleteAccountUseCase deleteAccountUseCase,
                             UpdateAccountUseCase updateAccountUseCase, GetAccountUseCase getAccountUseCase) {
        this.createAccountUseCase = createAccountUseCase;
        this.deleteAccountUseCase = deleteAccountUseCase;
        this.updateAccountUseCase = updateAccountUseCase;
        this.getAccountUseCase = getAccountUseCase;
    }

    @PostMapping
    public ResponseEntity<CreateAccountResult> createAccount(@RequestBody CreateAccountRequest request) {
        var command = new CreateAccountCommand(
            new Username(request.name()),
            Optional.ofNullable(request.email()).map(Email::new),
            new Password(request.password())
        );

        return ResponseEntity.ok(createAccountUseCase.execute(command));
    }

    @PatchMapping("/{id}")
    public void updateAccount(@PathVariable UUID id, @RequestBody UpdateAccountRequest request) {
        var command = new UpdateAccountCommand(
            new AccountId(id),
            new Username(request.name()),
            new Password(request.password()),
            Optional.ofNullable(request.newName()).map(Username::new),
            Optional.ofNullable(request.newPassword()).map(Password::new),
            Optional.ofNullable(request.newEmail()).map(Email::new)
        );

        updateAccountUseCase.execute(command);
    }

    @DeleteMapping("/{id}")
    public void deleteAccount(@PathVariable UUID id, @RequestBody DeleteAccountRequest request) {
        var command = new DeleteAccountCommand(
            new AccountId(id),
            new Username(request.name()),
            new Password(request.password())
        );

        deleteAccountUseCase.execute(command);
    }

    // TODO: Also return reviews and mediaItems belonging to this account
    @GetMapping("/{accountId}")
    public ResponseEntity<GetAccountResult> getAccount(@PathVariable UUID accountId, @RequestBody GetAccountRequest request) {
        var command = new GetAccountCommand(new AccountId(accountId), new Username(request.name()), new Password(request.password()));
        return ResponseEntity.ok(getAccountUseCase.execute(command));
    }
}
