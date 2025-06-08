package ru.otus.hw.repositories;

import jakarta.annotation.Nonnull;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.models.Book;

@Repository
public interface BookRepository extends ReactiveMongoRepository<Book, String>, BookRepositoryCustom {

    @Nonnull
    Mono<Book> findById(@Nonnull String id);

    @Nonnull
    Flux<Book> findAll();

    @Nonnull
    Mono<Book> save(@Nonnull Book book);

    Mono<Void> deleteById(@Nonnull String id);
}
