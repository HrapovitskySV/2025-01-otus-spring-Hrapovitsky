package ru.otus.hw.services;

import com.github.cloudyrock.spring.v5.EnableMongock;
//import de.flapdoodle.embed.mongo.spring.autoconfigure.EmbeddedMongoAutoConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import ru.otus.hw.converters.AuthorConverter;
import ru.otus.hw.converters.BookConverter;
import ru.otus.hw.converters.CommentConverter;
import ru.otus.hw.converters.GenreConverter;
import ru.otus.hw.models.dto.CommentDto;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@DisplayName("Сервис для работы с комментариями ")
@EnableMongock
@DataMongoTest
@Import({BookConverter.class, AuthorConverter.class, GenreConverter.class, CommentConverter.class, CommentServiceImpl.class })//BookRepository.class
class CommentServiceImplTest {


    private CommentService commentService;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CommentConverter commentConverter;

    @BeforeEach
    void setUp() {
        commentService = new CommentServiceImpl(commentRepository, bookRepository, commentConverter);
    }

    @DisplayName("У комментария должен получить книгу")
    @Test
    void findById() {
        var actualComment = commentService.findById(BigInteger.ONE);
        assertDoesNotThrow(() -> actualComment.get().getBook());
    }

    @Test
    void findByBookId() {
        var actualComments = commentService.findByBookId(BigInteger.ONE);

        assertDoesNotThrow(() -> actualComments.stream().map(CommentDto::getBook));
    }





}