package ru.otus.hw.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.converters.AuthorConverter;
import ru.otus.hw.converters.BookConverter;
import ru.otus.hw.converters.GenreConverter;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.models.dto.BookDto;
import ru.otus.hw.models.dto.BookDtoInputWeb;
import ru.otus.hw.models.dto.BookDtoWeb;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.GenreService;

import java.util.*;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;


@WebMvcTest(value = BookRestController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)// отключаю  Security в функциональных тестах
@Import({BookConverter.class, AuthorConverter.class, GenreConverter.class})
class BookRestControllerTest {

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

    @Autowired
    private ObjectMapper mapper;

    private final List<Book> books = List.of(new Book(1L, "Book1",null, new ArrayList<Genre>()),
            new Book(2L, "Book1", null,new ArrayList<Genre>()));


    @Test
    void listAllBooks() throws Exception {
        List<BookDto> expectedBooks = books.stream()
                .map(book -> bookConverter.toDto(book)).toList();
        List<BookDtoWeb> modelBooks = expectedBooks.stream()
                .map(book -> bookConverter.toDtoWeb(book)).toList();
        when(bookService.findAll()).thenReturn(expectedBooks);
        mvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(modelBooks)));
    }

    @Test
    void getBookTest() throws Exception {
        var book = books.get(0);
        when(bookService.findById(1L)).thenReturn(Optional.of(book));
        mvc.perform(get("/api/books/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(book)));
    }

    @Test
    void shouldCorrectSaveNewBook() throws Exception {
        Book book =  new Book(3L, "Book3", null,new ArrayList<Genre>());
        BookDtoInputWeb bookDtoInputWeb =  new BookDtoInputWeb(3L, "Book3", 0,new HashSet<Long>());
        String bookDtoInputWebString = mapper.writeValueAsString(bookDtoInputWeb);

        given(bookService.insert(anyString(),anyLong(),anySet())).willReturn(book);
        String expectedResult = mapper.writeValueAsString(book);

        mvc.perform(post("/api/books").contentType(APPLICATION_JSON)
                        .content(bookDtoInputWebString))
                .andExpect(status().isCreated())
                .andExpect(content().json(expectedResult));

    }


}