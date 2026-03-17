package org.a1cey.bookshelf_pro_application;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class BookshelfProApplicationTests {

    @Test
    void contextLoads() {
    }

}
