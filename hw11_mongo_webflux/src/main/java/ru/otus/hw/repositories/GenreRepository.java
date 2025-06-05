package ru.otus.hw.repositories;

import jakarta.annotation.Nonnull;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.models.Genre;

@Repository
public interface GenreRepository extends ReactiveMongoRepository<Genre, String> {

    @Nonnull
    Mono<Genre> findById(@Nonnull String id);

    Mono<Genre> findByName(String name);

    @NotNull
    Flux<Genre> findAllById(@NotNull Iterable<String> ids);

    @Nonnull
    Flux<Genre> findAll();

    @Nonnull
    Mono<Genre>  save(@Nonnull Genre genre);

    Mono<Void> deleteById(@Nonnull String id);

}