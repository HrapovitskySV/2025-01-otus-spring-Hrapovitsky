package ru.otus.hw.services;

import reactor.core.publisher.Mono;
import ru.otus.hw.models.Book;
import java.util.Set;

public interface BookService {
    Mono<Book>  insert(String title, String authorId, Set<String> genresIDs);

    Mono<Book> update(String id, String title, String authorId, Set<String> genresIDs);

    void deleteById(String id);
}
