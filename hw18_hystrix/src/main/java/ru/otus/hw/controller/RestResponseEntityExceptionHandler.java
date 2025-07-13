package ru.otus.hw.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.otus.hw.exceptions.AuthorNotFoundException;
import ru.otus.hw.exceptions.BookNotFoundException;
import ru.otus.hw.exceptions.EntityNotFoundException;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(value = { IllegalArgumentException.class, IllegalStateException.class })
    protected ResponseEntity<Object> handleConflict(RuntimeException ex, WebRequest request) {
        String bodyOfResponse = "This should be application specific";
        return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler(value = {BookNotFoundException.class, EntityNotFoundException.class })
    protected ResponseEntity<Object> handleBadRequest(RuntimeException ex, WebRequest request) {
        String bodyOfResponse = "Entity not found";
        return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(value = {AuthorNotFoundException.class})
    protected ResponseEntity<Object> handleAuthorNotFound(RuntimeException ex, WebRequest request) {
        String bodyOfResponse = "Author not found";
        return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }
}