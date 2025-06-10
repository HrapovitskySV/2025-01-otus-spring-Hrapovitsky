package ru.otus.hw.services;

import org.hibernate.LazyInitializationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.converters.AuthorConverter;
import ru.otus.hw.converters.BookConverter;
import ru.otus.hw.converters.CommentConverter;
import ru.otus.hw.converters.GenreConverter;
import ru.otus.hw.models.dto.CommentDto;
import ru.otus.hw.repositories.JdbcBookRepository;
import ru.otus.hw.repositories.JdbcCommentRepository;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("Сервис для работы с комментариями ")
@DataJpaTest
@Import({CommentServiceImpl.class, JdbcBookRepository.class, JdbcCommentRepository.class, CommentConverter.class, BookConverter.class, AuthorConverter.class, GenreConverter.class})
@Transactional(propagation = Propagation.NEVER)
class CommentServiceImplTest {

    @Autowired
    private CommentService commentService;

    @DisplayName("У комментария должен получить книгу")
    @Test
    void findById() {
        var actualComment = commentService.findById(1L);
        assertDoesNotThrow(() -> actualComment.get().getBookId());
    }

    @Test
    void findByBookId() {
        var actualComments = commentService.findByBookId(1L);

        assertDoesNotThrow(() -> actualComments.stream().map(CommentDto::getBookId).toList());
    }





}