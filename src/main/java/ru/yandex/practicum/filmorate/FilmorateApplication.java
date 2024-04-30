package ru.yandex.practicum.filmorate;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class FilmorateApplication {
    public static void main(String[] args) {
        log.trace("Starting {}", FilmorateApplication.class.getSimpleName());
        SpringApplication.run(FilmorateApplication.class, args);
    }

}