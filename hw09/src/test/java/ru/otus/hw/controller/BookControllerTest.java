package ru.otus.hw.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.converters.AuthorConverter;
import ru.otus.hw.converters.BookConverter;
import ru.otus.hw.converters.GenreConverter;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.models.dto.BookDto;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.GenreService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(BookController.class)
@Import({BookConverter.class, AuthorConverter.class, GenreConverter.class})//, AuthorService.class, GenreService.class
class BookControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private BookService bookService;

    @MockBean
    private AuthorService authorService;

    @MockBean
    private GenreService genreService;

    @Autowired
    private BookConverter bookConverter;

    private final List<Book> books = List.of(new Book(1L, "Book1",null, new ArrayList<Genre>()),
            new Book(2L, "Book1", null,new ArrayList<Genre>()));

    @Test
    void listAllBooks() throws Exception {
        List<BookDto> expectedBooks = books.stream()
                .map(book -> bookConverter.toDto(book)).toList();

        when(bookService.findAll()).thenReturn(expectedBooks);

        mvc.perform(get("/"))
                .andExpect(view().name("bookList"))
                .andExpect(model().attribute("books", expectedBooks));
    }

    @Test
    void editPage() throws Exception {
        Book book = books.get(0);
        when(authorService.findAll()).thenReturn(new ArrayList<Author>());
        when(genreService.findAll()).thenReturn(new ArrayList<Genre>());

        when(bookService.findById(1L)).thenReturn(Optional.of(book));
        mvc.perform(get("/edit").param("id", "1"))
            .andExpect(view().name("bookEdit"))
            .andExpect(model().attribute("book", book));

    }

    @Test
    void shouldRenderErrorPageWhenBookNotFound() throws Exception {
        when(authorService.findAll()).thenReturn(new ArrayList<Author>());
        when(genreService.findAll()).thenReturn(new ArrayList<Genre>());

        when(bookService.findById(1L)).thenThrow(new BookNotFoundException());
        mvc.perform(get("/edit").param("id", "1"))
                .andExpect(view().name("customError"));
    }

    @Test
    void shouldSaveBookAndRedirectToContextPath() throws Exception {
        when(bookService.findById(1L)).thenReturn(Optional.of(books.get(0)));
        mvc.perform(post("/edit").param("id", "3").param("fullName", "Толстой"))
                .andExpect(view().name("redirect:/"));
        verify(bookService, times(1)).save(any(Book.class));
    }

}