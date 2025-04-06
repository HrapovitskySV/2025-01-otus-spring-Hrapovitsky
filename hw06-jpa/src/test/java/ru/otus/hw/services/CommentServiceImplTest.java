package ru.otus.hw.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.converters.CommentConverter;
import ru.otus.hw.models.dto.CommentDto;
import ru.otus.hw.repositories.JdbcBookRepository;
import ru.otus.hw.repositories.JdbcCommentRepository;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@DisplayName("Сервис для работы с комментариями ")
@DataJpaTest
@Import({JdbcBookRepository.class, JdbcCommentRepository.class, CommentConverter.class})
@Transactional(propagation = Propagation.NEVER)
class CommentServiceImplTest {


    private CommentService commentService;

    @Autowired
    private JdbcCommentRepository commentRepository;

    @Autowired
    private JdbcBookRepository bookRepository;

    @Autowired
    private CommentConverter commentConverter;

    @BeforeEach
    void setUp() {
        commentService = new CommentServiceImpl(commentRepository, bookRepository,  commentConverter);
    }

    @DisplayName("У комментария должен получить книгу")
    @Test
    void findById() {
        var actualComment = commentService.findById(1L);
        assertDoesNotThrow(() -> actualComment.get().getBook());
    }

    @Test
    void findByBookId() {
        var actualComments = commentService.findByBookId(1L);

        assertDoesNotThrow(() -> actualComments.stream().map(CommentDto::getBook));
    }





}