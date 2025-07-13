package ru.otus.hw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {
	// http://localhost:8080/
	// http://localhost:8080/api/authors
	// http://localhost:8080/api/books

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}