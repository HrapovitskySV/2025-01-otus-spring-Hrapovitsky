package ru.otus.hw.controller;

public class AuthorNotFoundException  extends RuntimeException {

    AuthorNotFoundException() {
        super("Author not found");
    }
}
