package org.a1cey.bookshelf_pro_domain.user;

import org.apache.commons.validator.routines.EmailValidator;
import org.jmolecules.ddd.annotation.ValueObject;

@ValueObject
public record Email(@jakarta.validation.constraints.Email String email) {

    private static final EmailValidator validator = EmailValidator.getInstance();

    public Email {
        if (!validator.isValid(email)) {
            throw new IllegalArgumentException("Email " + email + " is not valid.");
        }
    }

}
