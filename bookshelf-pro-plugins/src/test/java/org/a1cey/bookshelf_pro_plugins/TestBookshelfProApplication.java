package org.a1cey.bookshelf_pro_plugins;

import org.springframework.boot.SpringApplication;


public class TestBookshelfProApplication {

    static void main(String[] args) {
        SpringApplication.from(BookshelfProApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
