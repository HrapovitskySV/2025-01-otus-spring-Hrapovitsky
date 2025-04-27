package ru.otus.hw.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import ru.otus.hw.converters.AuthorConverter;
import ru.otus.hw.converters.BookConverter;
import ru.otus.hw.converters.CommentConverter;
import ru.otus.hw.converters.GenreConverter;
import ru.otus.hw.models.dto.CommentDto;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@DisplayName("Сервис для работы с комментариями ")
@DataMongoTest
@Import({BookConverter.class, AuthorConverter.class, GenreConverter.class, CommentConverter.class, CommentServiceImpl.class })//BookRepository.class
class CommentServiceImplTest {

    @Autowired
    private CommentService commentService;


    @DisplayName("У комментария должен получить книгу")
    @Test
    void findById() {
        var actualComment = commentService.findById("1");
        assertDoesNotThrow(() -> actualComment.get().getBook());
    }

    @Test
    void findByBookId() {
        var actualComments = commentService.findByBookId("1");

        assertDoesNotThrow(() -> actualComments.stream().map(CommentDto::getBook));
    }





}