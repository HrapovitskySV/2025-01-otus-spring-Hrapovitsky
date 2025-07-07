package ru.otus.hw.repositories;

import jakarta.annotation.Nonnull;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.models.Author;


@Repository
public interface AuthorRepository extends ReactiveMongoRepository<Author, String> {

    @Nonnull
    Mono<Author> findById(@Nonnull String id);

    Flux<Author> findByFullName(String fullName);

    @Nonnull
    Flux<Author> findAll();

    @Nonnull
    Mono<Author>  save(@Nonnull Author author);

    Mono<Void> deleteById(@Nonnull String id);

}

