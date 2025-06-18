package ru.otus.hw.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.converters.AuthorConverter;
import ru.otus.hw.converters.BookConverter;
import ru.otus.hw.converters.GenreConverter;
import ru.otus.hw.exceptions.BookNotFoundException;
import ru.otus.hw.models.*;
import ru.otus.hw.security.SecurityConfiguration;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.CustomUserDetailsService;
import ru.otus.hw.services.GenreService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(value = BookPagesController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)// отключаю  Security в функциональных тестах
@Import({BookConverter.class, AuthorConverter.class, GenreConverter.class})//, AuthorService.class, GenreService.class
class BookPagesControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private BookService bookService;

    @MockBean
    private AuthorService authorService;

    @MockBean
    private GenreService genreService;


    private final List<Book> books = List.of(new Book(1L, "Book1",null, new ArrayList<Genre>()),
            new Book(2L, "Book1", null,new ArrayList<Genre>()));



    @Test
    void listAllBooks() throws Exception {
        mvc.perform(get("/"))
                .andExpect(view().name("bookList"));
    }

    @Test
    void editPage() throws Exception {
        Book book = books.get(0);
        when(authorService.findAll()).thenReturn(new ArrayList<Author>());
        when(genreService.findAll()).thenReturn(new ArrayList<Genre>());

        when(bookService.findById(1L)).thenReturn(Optional.of(book));
        mvc.perform(get("/edit/1"))
            .andExpect(view().name("bookEdit"))
            .andExpect(model().attribute("book", book));

    }

    @Test
    void shouldRenderErrorPageWhenBookNotFound() throws Exception {
        when(authorService.findAll()).thenReturn(new ArrayList<Author>());
        when(genreService.findAll()).thenReturn(new ArrayList<Genre>());

        when(bookService.findById(1L)).thenThrow(new BookNotFoundException());
        mvc.perform(get("/edit/1"))
                .andExpect(view().name("customError"));
    }
}