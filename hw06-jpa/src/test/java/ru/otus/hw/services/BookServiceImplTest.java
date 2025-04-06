package ru.otus.hw.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import ru.otus.hw.converters.AuthorConverter;
import ru.otus.hw.converters.BookConverter;
import ru.otus.hw.converters.GenreConverter;
import ru.otus.hw.models.dto.BookDto;
import ru.otus.hw.repositories.JdbcAuthorRepository;
import ru.otus.hw.repositories.JdbcBookRepository;
import ru.otus.hw.repositories.JdbcGenreRepository;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@DisplayName("Сервис для работы с книгами ")
@DataJpaTest
@Import({JdbcBookRepository.class, JdbcAuthorRepository.class, JdbcGenreRepository.class, BookConverter.class, AuthorConverter.class, GenreConverter.class })
class BookServiceImplTest {


    private BookService bookService;


    @Autowired
    private JdbcBookRepository bookRepository;

    @Autowired
    private JdbcAuthorRepository authorRepository;


    @Autowired
    private BookConverter bookConverter;

    @Autowired
    private JdbcGenreRepository genreRepository;

    @BeforeEach
    void setUp() {
        bookService = new BookServiceImpl(authorRepository, genreRepository, bookRepository, bookConverter);
    }

    @DisplayName("У книги должен получить автора и жанры")
    @Test
    void findById() {
        var actualBook = bookService.findById(1L);
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