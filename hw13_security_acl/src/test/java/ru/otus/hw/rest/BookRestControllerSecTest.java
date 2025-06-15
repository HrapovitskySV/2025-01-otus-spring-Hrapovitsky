package ru.otus.hw.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.converters.AuthorConverter;
import ru.otus.hw.converters.BookConverter;
import ru.otus.hw.converters.GenreConverter;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.models.dto.BookDto;
import ru.otus.hw.models.dto.BookDtoInputWeb;
import ru.otus.hw.models.dto.BookDtoWeb;
import ru.otus.hw.security.SecurityConfiguration;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.GenreService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(value = BookRestController.class)
@Import({BookConverter.class, AuthorConverter.class, GenreConverter.class, SecurityConfiguration.class})// включаю настройки Security
class BookRestControllerSecTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private BookService bookService;

    @Test
    void testAuthenticatedOnUser() throws Exception {
        mvc.perform(get("/api/books").with(user("USER").roles("USER")))
                .andExpect(status().isOk());
        mvc.perform(get("/api/books/1").with(user("USER").roles("USER")))
                .andExpect(status().isOk());
    }

    @Test
    void testBookListWithoutAuth() throws Exception {
        mvc.perform(get("/api/books"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrlPattern("**/login"));
    }



}