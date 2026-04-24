package org.a1cey.bookshelf_pro_plugins.rest.account;

import java.util.Optional;

import org.a1cey.bookshelf_pro_application.account.CreateAccountUseCase;
import org.a1cey.bookshelf_pro_application.account.DeleteAccountUseCase;
import org.a1cey.bookshelf_pro_application.account.GetAccountUseCase;
import org.a1cey.bookshelf_pro_application.account.UpdateAccountUseCase;
import org.a1cey.bookshelf_pro_application.account.command.CreateAccountCommand;
import org.a1cey.bookshelf_pro_application.account.command.UpdateAccountCommand;
import org.a1cey.bookshelf_pro_application.account.result.CreateAccountResult;
import org.a1cey.bookshelf_pro_application.account.result.GetAccountResult;
import org.a1cey.bookshelf_pro_domain.account.Email;
import org.a1cey.bookshelf_pro_domain.account.Username;
import org.a1cey.bookshelf_pro_plugins.rest.account.request.CreateAccountRequest;
import org.a1cey.bookshelf_pro_plugins.rest.account.request.UpdateAccountRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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
            new Username(request.username()),
            Optional.ofNullable(request.email()).map(Email::new),
            request.rawPassword()
        );

        return ResponseEntity.ok(createAccountUseCase.execute(command));
    }

    @PatchMapping
    public void updateAccount(@RequestBody UpdateAccountRequest request) {
        var command = new UpdateAccountCommand(
            Optional.ofNullable(request.newName()).map(Username::new),
            Optional.ofNullable(request.newRawPassword()),
            Optional.ofNullable(request.newEmail()).map(Email::new)
        );

        updateAccountUseCase.execute(command);
    }

    @DeleteMapping
    public void deleteAccount() {
        deleteAccountUseCase.execute();
    }

    @GetMapping
    public ResponseEntity<GetAccountResult> getAccount() {
        return ResponseEntity.ok(getAccountUseCase.execute());
    }
}
