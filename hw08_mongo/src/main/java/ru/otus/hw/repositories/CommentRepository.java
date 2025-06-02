package ru.otus.hw.repositories;

import jakarta.annotation.Nonnull;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends MongoRepository<Comment, String> {

    @Nonnull
    Optional<Comment> findById(@Nonnull String id);

    @Nonnull
    List<Comment>  findAll();

    List<Comment> findByBook(Book book);

    @Nonnull
    Comment save(@Nonnull Comment comment);

    void deleteById(@Nonnull String id);

    void deleteByBook(Book book);
}
