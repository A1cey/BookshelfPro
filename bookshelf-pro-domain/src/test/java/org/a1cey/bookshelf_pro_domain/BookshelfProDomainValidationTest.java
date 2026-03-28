package org.a1cey.bookshelf_pro_domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.validation.autoconfigure.ValidationAutoConfiguration;
import org.springframework.test.context.ContextConfiguration;

import jakarta.validation.Validator;

@ContextConfiguration(classes = ValidationAutoConfiguration.class)
@SpringBootTest
class BookshelfProDomainValidationTest {

    @Autowired
    Validator validator;

    @Test
    void validatorIsAvailable() {
        // Use this class as a base for validation-focused domain tests
    }

}