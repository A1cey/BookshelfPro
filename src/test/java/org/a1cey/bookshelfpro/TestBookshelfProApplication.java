package org.a1cey.bookshelfpro;

import org.springframework.boot.SpringApplication;

public class TestBookshelfProApplication {

    public static void main(String[] args) {
        SpringApplication.from(BookshelfProApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
