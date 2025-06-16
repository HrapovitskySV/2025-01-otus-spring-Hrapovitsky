package ru.otus.hw.controller;

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
import ru.otus.hw.services.GenreService;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest({BookPagesController.class, AuthorPagesController.class})
@Import({BookConverter.class, AuthorConverter.class, GenreConverter.class, SecurityConfiguration.class})// включаю настройки Security  //, AuthorService.class, GenreService.class
class PagesControllerSecurityTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private BookService bookService;

    @MockBean
    private AuthorService authorService;

    @MockBean
    private GenreService genreService;


    @ParameterizedTest
    @ValueSource(strings = {"/", "/edit/1", "/add","/authors/", "/authors/edit/1", "/authors/add"})
    void testAuthenticatedOnUser(String url) throws Exception {
        mvc.perform(get(url).with(user("USER").roles("USER")))
                .andExpect(status().isOk());
    }

    @ParameterizedTest
    @ValueSource(strings = {"/", "/edit/1", "/add","/authors/", "/authors/edit/1", "/authors/add"})
    void testBookListWithoutAuth(String url) throws Exception {
        mvc.perform(get(url))
                .andExpect(status().isFound())
                .andExpect(redirectedUrlPattern("**/login"));
    }
}