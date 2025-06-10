package ru.otus.hw.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
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

@WebMvcTest(BookPagesController.class)
@Import({BookConverter.class, AuthorConverter.class, GenreConverter.class, SecurityConfiguration.class})//, AuthorService.class, GenreService.class
class BookPagesControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private BookService bookService;

    @MockBean
    private AuthorService authorService;

    @MockBean
    private GenreService genreService;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;


    private final List<Book> books = List.of(new Book(1L, "Book1",null, new ArrayList<Genre>()),
            new Book(2L, "Book1", null,new ArrayList<Genre>()));

    @BeforeEach
    void login(){
        when(customUserDetailsService.loadUserByUsername(any())).
                thenReturn(new CustomUser(1,"USER","1", List.of(new Role(1,"USER"))));
    }

    @Test
    @WithMockUser(username = "USER",roles = {"USER"})
    void listAllBooks() throws Exception {
        mvc.perform(get("/authenticated/"))
                .andExpect(view().name("bookList"));
    }

    @Test
    @WithMockUser(username = "USER",roles = {"USER"})
    void editPage() throws Exception {
        Book book = books.get(0);
        when(authorService.findAll()).thenReturn(new ArrayList<Author>());
        when(genreService.findAll()).thenReturn(new ArrayList<Genre>());

        when(bookService.findById(1L)).thenReturn(Optional.of(book));
        mvc.perform(get("/authenticated/edit/1"))
            .andExpect(view().name("bookEdit"))
            .andExpect(model().attribute("book", book));

    }

    @Test
    @WithMockUser(username = "USER",roles = {"USER"})
    void shouldRenderErrorPageWhenBookNotFound() throws Exception {
        when(authorService.findAll()).thenReturn(new ArrayList<Author>());
        when(genreService.findAll()).thenReturn(new ArrayList<Genre>());

        when(bookService.findById(1L)).thenThrow(new BookNotFoundException());
        mvc.perform(get("/authenticated/edit/1"))
                .andExpect(view().name("customError"));
    }

    @Test
    void testAuthenticatedOnUser() throws Exception {
        mvc.perform(get("/authenticated/").with(user("USER").roles("USER")))
                .andExpect(status().isOk());
        mvc.perform(get("/authenticated/edit/1").with(user("USER").roles("USER")))
                .andExpect(status().isOk());
        mvc.perform(get("/authenticated/add").with(user("USER").roles("USER")))
                .andExpect(status().isOk());
    }
}