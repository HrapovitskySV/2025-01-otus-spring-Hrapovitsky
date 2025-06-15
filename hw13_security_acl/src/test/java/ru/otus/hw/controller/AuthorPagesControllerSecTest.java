package ru.otus.hw.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.exceptions.AuthorNotFoundException;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.CustomUser;
import ru.otus.hw.models.Role;
import ru.otus.hw.security.SecurityConfiguration;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.CustomUserDetailsService;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(AuthorPagesController.class)
@Import({SecurityConfiguration.class})  // включаю настройки Security
class AuthorPagesControllerSecTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private AuthorService authorService;

     @Test
    void testAuthenticatedOnUser() throws Exception {
        mvc.perform(get("/authors/").with(user("USER").roles("USER")))
                .andExpect(status().isOk());
        mvc.perform(get("/authors/edit/1").with(user("USER").roles("USER")))
                .andExpect(status().isOk());
        mvc.perform(get("/authors/add").with(user("USER").roles("USER")))
                .andExpect(status().isOk());
    }

    @Test
    void testAuthorListWithoutAuth() throws Exception {
        mvc.perform(get("/authors/"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrlPattern("**/login"));
    }
}