package ru.otus.hw.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import ru.otus.hw.converters.AuthorConverter;
import ru.otus.hw.converters.BookConverter;
import ru.otus.hw.converters.GenreConverter;
import ru.otus.hw.exceptions.AuthorNotFoundException;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.CustomUser;
import ru.otus.hw.models.Role;
import ru.otus.hw.security.SecurityConfiguration;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.CustomUserDetailsService;

import java.util.List;
import java.util.Optional;


import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;



@WebMvcTest(AuthorPagesController.class)
@Import({SecurityConfiguration.class})
class AuthorPagesControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private AuthorService authorService;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    private final List<Author> authors = List.of(new Author(1L, "Пушкин"),
            new Author(2L, "Лермонтов"));

    @BeforeEach
    void login(){
        when(customUserDetailsService.loadUserByUsername(any())).
                thenReturn(new CustomUser(1,"USER","1", List.of(new Role(1,"USER"))));
    }

    @Test
    @WithMockUser(username = "USER",roles = {"USER"})
    void listAllAuthors() throws Exception {
        mvc.perform(get("/authenticated/authors/"))
                .andExpect(view().name("authorList"));

    }

    @Test
    @WithMockUser(username = "USER",roles = {"USER"})
    void editPage() throws Exception {
        Author author = authors.get(0);
        when(authorService.findById(1L)).thenReturn(Optional.of(author));
        mvc.perform(get("/authenticated/authors/edit/1"))
            .andExpect(view().name("authorEdit"))
            .andExpect(model().attribute("author", author));

    }

    @Test
    @WithMockUser(username = "USER",roles = {"USER"})
    void shouldRenderErrorPageWhenAuthorNotFound() throws Exception {
        when(authorService.findById(1L)).thenThrow(new AuthorNotFoundException());
        mvc.perform(get("/authenticated/authors/edit/1"))
                .andExpect(view().name("customError"));
    }


    @Test
    void testAuthenticatedOnUser() throws Exception {
        mvc.perform(get("/authenticated/authors/").with(user("USER").roles("USER")))
                .andExpect(status().isOk());
        mvc.perform(get("/authenticated/authors/edit/1").with(user("USER").roles("USER")))
                .andExpect(status().isOk());
        mvc.perform(get("/authenticated/authors/add").with(user("USER").roles("USER")))
                .andExpect(status().isOk());
    }
}