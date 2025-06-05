package ru.otus.hw.services;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.dto.BookDto;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface BookService {
    Mono<Book> findById(String id);

    Flux<BookDto> findAll();

    Mono<Book>  insert(String title, String authorId, Set<String> genresIDs);

    Mono<Book> update(String id, String title, String authorId, Set<String> genresIDs);

    void deleteById(String id);
}
