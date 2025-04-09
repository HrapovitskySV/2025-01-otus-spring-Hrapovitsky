package ru.otus.hw.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends MongoRepository<Comment, BigInteger> {

    Optional<Comment> findById(BigInteger id);

    List<Comment> findAll();

    List<Comment> findByBook(Book book);

    Comment save(Comment comment);

    void deleteById(BigInteger id);

    void deleteByBook(Book book);
}
