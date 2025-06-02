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
import ru.otus.hw.models.dto.BookDto;


import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@DisplayName("Сервис для работы с книгами ")
@Import({BookServiceImpl.class, AuthorServiceImpl.class, GenreServiceImpl.class, BookConverter.class, AuthorConverter.class, GenreConverter.class, CommentConverter.class, CommentServiceImpl.class })
@DataMongoTest
class BookServiceImplTest {

    @Autowired
    private BookService bookService;

    @DisplayName("У книги должен получить автора и жанры")
    @Test
    void findById() {
        var actualBook = bookService.findById("1");
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