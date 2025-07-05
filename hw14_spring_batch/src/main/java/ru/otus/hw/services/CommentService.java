package ru.otus.hw.services;

import ru.otus.hw.modelsMongo.BookMongo;
import ru.otus.hw.modelsMongo.CommentMongo;
import ru.otus.hw.modelsMongo.dto.CommentMongoDto;

import java.util.List;
import java.util.Optional;


public interface CommentService {
    Optional<CommentMongo> findById(String id);

    List<CommentMongoDto> findAll();

    List<CommentMongoDto> findByBookId(String bookId);

    CommentMongo insert(String comment, String bookId);

    CommentMongo update(String id, String comment, String bookId);

    void deleteById(String id);

    void deleteByBookId(String bookId);

    void deleteByBook(BookMongo book);
}
