package ru.otus.hw.controller;

public class BookNotFoundException extends RuntimeException {

    BookNotFoundException() {
        super("Book not found");
    }
}
