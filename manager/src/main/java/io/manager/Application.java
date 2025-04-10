package io.manager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;


//TODO: счётчик - плохонужно запоминать какие воркеры выполнили а какие нет, синхронизация реквестов

@SpringBootApplication()
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
