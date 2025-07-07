package ru.otus.hw.repositories;

import jakarta.annotation.Nonnull;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;


@Repository
public interface CommentRepository extends ReactiveMongoRepository<Comment, String> {

    @Nonnull
    Mono<Comment> findById(@Nonnull String id);

    @Nonnull
    Flux<Comment> findAll();

    Flux<Comment> findByBook(Book book);

    @Nonnull
    Mono<Comment> save(@Nonnull Comment comment);

    Mono<Void> deleteById(@Nonnull String id);

    Mono<Void> deleteByBook(Book book);

    Mono<Void> deleteByBookId(@Nonnull String bookId);
}
