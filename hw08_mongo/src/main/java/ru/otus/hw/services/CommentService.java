package ru.otus.hw.services;

import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.dto.CommentDto;

import java.util.List;
import java.util.Optional;


public interface CommentService {
    Optional<Comment> findById(String id);

    List<CommentDto> findAll();

    List<CommentDto> findByBookId(String bookId);

    Comment insert(String comment, String bookId);

    Comment update(String id, String comment, String bookId);

    void deleteById(String id);

    void deleteByBookId(String bookId);

    void deleteByBook(Book book);
}
