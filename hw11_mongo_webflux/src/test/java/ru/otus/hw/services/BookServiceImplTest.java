package ru.otus.hw.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.parallel.ExecutionMode.SAME_THREAD;
import static org.mockito.Mockito.*;
import org.springframework.context.annotation.Import;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.converters.AuthorConverter;
import ru.otus.hw.converters.BookConverter;
import ru.otus.hw.converters.CommentConverter;
import ru.otus.hw.converters.GenreConverter;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.Set;


@DisplayName("Сервис для работы с книгами ")
@Import({BookServiceImpl.class, AuthorServiceImpl.class, BookConverter.class, AuthorConverter.class, GenreConverter.class, CommentConverter.class, CommentServiceImpl.class})
@DataMongoTest
class BookServiceImplTest {

    @Autowired
    private BookService bookService;


    @MockBean
    private BookRepository bookRepository;

    @MockBean
    private AuthorRepository authorRepository;

    @MockBean
    private GenreRepository genreRepository;

    @MockBean
    private CommentRepository commentRepository;



    @DisplayName("Добавление книги")
    @Test
    void insertBook() {
        when(genreRepository.findAllById((Iterable<String>) any())).thenReturn(Flux.just(new Genre("1","Genre1")));
        when(authorRepository.findById((String) any())).thenReturn(Mono.just(new Author("1","Author1")));
        bookService.insert("10", "1", Set.of("1")).subscribe();
        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @DisplayName("Добавление книги c пустыми жанрами")
    @Test
    void insertBook2() {
        when(genreRepository.findAllById((Iterable<String>) any())).thenReturn(Flux.just(new Genre("1","Genre1")));
        when(authorRepository.findById((String) any())).thenReturn(Mono.just(new Author("1","Author1")));
        assertThrows(IllegalArgumentException.class,() ->bookService.insert("10", "1", Set.of()).subscribe());
    }

    @DisplayName("Обновление книги")
    @Test
    void updateBook() {
        when(genreRepository.findAllById((Iterable<String>) any())).thenReturn(Flux.just(new Genre("1","Genre1")));
        when(authorRepository.findById((String) any())).thenReturn(Mono.just(new Author("1","Author1")));
        bookService.update("1", "20", "1", Set.of("1")).subscribe();
        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @DisplayName("Удаление книги")
    @Test
    void deleteBook() {
        bookService.deleteById("1");
        verify(commentRepository, times(1)).deleteByBookId("1");
        verify(bookRepository, times(1)).deleteById("1");
    }

}