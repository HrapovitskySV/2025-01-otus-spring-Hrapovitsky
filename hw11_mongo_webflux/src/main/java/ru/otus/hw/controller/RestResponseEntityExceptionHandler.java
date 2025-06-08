package ru.otus.hw.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.handler.ResponseStatusExceptionHandler;
import reactor.core.publisher.Mono;
import ru.otus.hw.exceptions.AuthorNotFoundException;
import ru.otus.hw.exceptions.BookNotFoundException;
import ru.otus.hw.exceptions.EntityNotFoundException;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseStatusExceptionHandler {
    @ExceptionHandler(value = { IllegalArgumentException.class, IllegalStateException.class })
    protected Mono<ResponseEntity<Object>> handleConflict(RuntimeException ex, WebRequest request) {
        String bodyOfResponse = "This should be application specific";
        return Mono.just(ResponseEntity.status(HttpStatus.CONFLICT).body(bodyOfResponse));
    }

    @ExceptionHandler(value = {BookNotFoundException.class, EntityNotFoundException.class })
    protected Mono<ResponseEntity<Object>> handleBadRequest(RuntimeException ex, WebRequest request) {
        String bodyOfResponse = "Entity not found";
        return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(bodyOfResponse));
    }

    @ExceptionHandler(value = {AuthorNotFoundException.class})
    protected Mono<ResponseEntity<Object>> handleAuthorNotFound(RuntimeException ex, WebRequest request) {
        String bodyOfResponse = "Author not found";
        return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(bodyOfResponse));
    }
}