package com.librosonline;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class LibrosonlineApplication {

    public static void main(String[] args) {
        SpringApplication.run(LibrosonlineApplication.class, args);
    }
}
