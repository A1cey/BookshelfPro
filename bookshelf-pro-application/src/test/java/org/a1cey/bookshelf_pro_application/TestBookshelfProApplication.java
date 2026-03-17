package org.a1cey.bookshelf_pro_application;

import org.springframework.boot.SpringApplication;


public class TestBookshelfProApplication {

    public static void main(String[] args) {
        SpringApplication.from(BookshelfProApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
