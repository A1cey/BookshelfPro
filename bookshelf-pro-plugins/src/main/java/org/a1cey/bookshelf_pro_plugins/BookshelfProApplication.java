package org.a1cey.bookshelf_pro_plugins;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {
    "org.a1cey.bookshelf_pro_plugins",
    "org.a1cey.bookshelf_pro_application"
})
public class BookshelfProApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookshelfProApplication.class, args);
    }

}
