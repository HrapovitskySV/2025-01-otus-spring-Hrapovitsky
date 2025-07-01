package ru.otus.hw.services;

import ru.otus.hw.modelsJpa.Comment;
import ru.otus.hw.modelsJpa.dto.CommentDto;

import java.util.List;
import java.util.Optional;

public interface CommentService {
    Optional<CommentDto> findById(long id);

    List<CommentDto> findByBookId(Long bookId);

    Comment insert(String comment, long bookId);

    Comment update(long id, String comment, long bookId);

    void deleteById(long id);
}
