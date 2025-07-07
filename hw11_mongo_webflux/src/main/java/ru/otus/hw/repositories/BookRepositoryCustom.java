package ru.otus.hw.repositories;

import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface BookRepositoryCustom {

    Mono<Boolean> updateBookAuthors(String authorId, String authorFullName);

    Mono<Boolean> deleteBookAuthors(String authorId);
}
