package org.a1cey.bookshelf_pro_plugins.rest.account;

import java.util.Optional;

import org.a1cey.bookshelf_pro_application.account.CreateAccountUseCase;
import org.a1cey.bookshelf_pro_application.account.command.CreateAccountCommand;
import org.a1cey.bookshelf_pro_application.account.result.CreateAccountResult;
import org.a1cey.bookshelf_pro_domain.account.Email;
import org.a1cey.bookshelf_pro_domain.account.Password;
import org.a1cey.bookshelf_pro_domain.account.Username;
import org.a1cey.bookshelf_pro_plugins.rest.account.request.CreateAccountRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/account")
public class AccountController {
    private final CreateAccountUseCase createAccountUseCase;

    public AccountController(CreateAccountUseCase createAccountUseCase) {
        this.createAccountUseCase = createAccountUseCase;
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
}
