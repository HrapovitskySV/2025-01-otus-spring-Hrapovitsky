package ru.otus.hw.services;

import com.github.cloudyrock.spring.v5.EnableMongock;
//import de.flapdoodle.embed.mongo.spring.autoconfigure.EmbeddedMongoAutoConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.otus.hw.converters.AuthorConverter;
import ru.otus.hw.converters.BookConverter;
import ru.otus.hw.converters.CommentConverter;
import ru.otus.hw.converters.GenreConverter;
import ru.otus.hw.models.dto.BookDto;
import ru.otus.hw.repositories.BookRepository;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@DisplayName("Сервис для работы с книгами ")
@EnableMongock
@Import({BookConverter.class, AuthorConverter.class, GenreConverter.class, CommentConverter.class, CommentServiceImpl.class })
@DataMongoTest
class BookServiceImplTest {


    private BookService bookService;


    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CommentService commentService;


    @Autowired
    private BookConverter bookConverter;


    @BeforeEach
    void setUp() {
        bookService = new BookServiceImpl(bookRepository, commentService, bookConverter);
    }

    @DisplayName("У книги должен получить автора и жанры")
    @Test
    void findById() {
        var actualBook = bookService.findById(BigInteger.ONE);
        assertDoesNotThrow(() -> actualBook.get().getAuthor());
        assertDoesNotThrow(() -> actualBook.get().getGenres());
    }

    @DisplayName("У всех книг должен получить автора и жанры")
    @Test
    void findAll() {
        var actualBooks = bookService.findAll();

        assertDoesNotThrow(() -> actualBooks.stream().map(BookDto::getAuthor));
        assertDoesNotThrow(() -> actualBooks.stream().map(BookDto::getGenres));
    }
}