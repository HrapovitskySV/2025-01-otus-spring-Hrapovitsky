package ru.otus.hw.services;

import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.dto.CommentDto;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;


public interface CommentService {
    Optional<Comment> findById(BigInteger id);

    List<CommentDto> findAll();

    List<CommentDto> findByBookId(BigInteger bookId);

    Comment insert(String comment, BigInteger bookId);

    Comment update(BigInteger id, String comment, BigInteger bookId);

    void deleteById(BigInteger id);

    void deleteByBookId(BigInteger bookId);

    void deleteByBook(Book book);
}
