package ru.otus.hw.repositories;

import jakarta.annotation.Nonnull;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.hw.models.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Optional<Comment> findById(long id);

    @Nonnull
    List<Comment> findByBookId(long bookId);

    @Nonnull
    Comment save(@Nonnull Comment comment);

    void deleteById(long id);

    @Override
    long count();
}
