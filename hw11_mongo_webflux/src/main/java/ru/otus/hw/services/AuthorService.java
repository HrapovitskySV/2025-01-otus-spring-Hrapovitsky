package ru.otus.hw.services;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.models.Author;

import java.util.List;
import java.util.Optional;


public interface AuthorService {
    Mono<Author> findById(String id);

    Flux<Author> findAll();

    Mono<Author>  save(Author author);

    Mono<Void> deleteById(String id);
}
