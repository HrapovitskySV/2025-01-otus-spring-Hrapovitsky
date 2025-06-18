package ru.otus.hw.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;
import ru.otus.hw.models.dto.CommentDto;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;

import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("Сервис для работы с комментариями ")
@DataMongoTest
@Import({BookConverter.class, AuthorConverter.class, GenreConverter.class, CommentConverter.class, CommentServiceImpl.class })//BookRepository.class
class CommentServiceImplTest {

    @Autowired
    private CommentService commentService;

    @MockBean
    private BookRepository bookRepository;

    @MockBean
    private CommentRepository commentRepository;


    @DisplayName("Добавление комментария")
    @Test
    void insertComment() {
        when(bookRepository.findById((String) any())).thenReturn(Mono.just(new Book("1","Title 1",null, List.of())));
        commentService.save("10", "text", "1").subscribe();
        verify(commentRepository, times(1)).save(any(Comment.class));
    }
    @DisplayName("Добавление комментария к несуществующей книги")
    @Test
    void insertComment2() {
        when(bookRepository.findById((String) any())).thenReturn(Mono.empty());
        assertThrows(EntityNotFoundException.class,() ->commentService.save("10", "text", "1").block());
    }


}