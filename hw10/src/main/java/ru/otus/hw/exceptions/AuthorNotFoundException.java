package ru.otus.hw.exceptions;

public class AuthorNotFoundException  extends RuntimeException {

    public AuthorNotFoundException() {
        super("Author not found");
    }
}
