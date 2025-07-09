package ru.otus.hw.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.converters.AuthorConverter;
import ru.otus.hw.converters.BookConverter;
import ru.otus.hw.converters.GenreConverter;
import ru.otus.hw.models.dto.AuthorDto;
import ru.otus.hw.models.dto.BookDto;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@DisplayName("Сервис для работы с книгами ")
@DataJpaTest(excludeAutoConfiguration = SecurityAutoConfiguration.class)
@Import({BookServiceImpl.class, BookServiceWrapperServiceImpl.class, BookConverter.class, AuthorConverter.class, GenreConverter.class})//, AclServiceWrapperService.class
@Transactional(propagation = Propagation.NEVER)
class BookServiceWrapperServiceTest {

    @Autowired
    private BookServiceWrapperService bookService;

    @MockBean
    private AclServiceWrapperService aclServiceWrapperService;


    @DisplayName("У книги должен получить автора и жанры")
    @Test
    void findById() {
        var actualBook = bookService.findById(1L);
        assertDoesNotThrow(() -> actualBook.get().getAuthor().getFullName());
        assertDoesNotThrow(() -> actualBook.get().getGenres().get(0).getName());
    }

    @DisplayName("У всех книг должен получить автора и жанры")
    @Test
    void findAll() {
        var actualBooks = bookService.findAll();

        assertDoesNotThrow(() -> actualBooks.stream().map(BookDto::getAuthor).map(AuthorDto::getFullName));

        assertDoesNotThrow(() -> actualBooks.stream().map(book -> book.getGenres().get(0).getName()));
    }





}