package ru.otus.hw.rest;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.converters.AuthorConverter;
import ru.otus.hw.converters.BookConverter;
import ru.otus.hw.converters.GenreConverter;
import ru.otus.hw.security.SecurityConfiguration;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(value = {BookRestController.class, AuthorRestController.class})
@Import({BookConverter.class, AuthorConverter.class, GenreConverter.class, SecurityConfiguration.class})// включаю настройки Security
class RestControllerSecurityTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private BookService bookService;

    @MockBean
    private AuthorService authorService;


    @ParameterizedTest
    @ValueSource(strings = {"/api/books", "/api/books/1", "/api/authors","/api/authors/1"})
    void testAuthenticatedOnUser(String url) throws Exception {
        mvc.perform(get(url).with(user("USER").roles("USER")))
                .andExpect(status().isOk());
    }

    @ParameterizedTest
    @ValueSource(strings = {"/api/books", "/api/books/1", "/api/authors","/api/authors/1"})
    void testBookListWithoutAuth(String url) throws Exception {
        mvc.perform(get(url))
                .andExpect(status().isFound())
                .andExpect(redirectedUrlPattern("**/login"));
    }



}