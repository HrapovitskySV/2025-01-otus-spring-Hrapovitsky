package ru.otus.hw.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import ru.otus.hw.exceptions.AuthorNotFoundException;
import ru.otus.hw.models.Author;
import ru.otus.hw.services.AuthorService;

import java.util.List;
import java.util.Optional;


import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(AuthorPagesController.class)
class AuthorPagesControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private AuthorService authorService;

    private final List<Author> authors = List.of(new Author(1L, "Пушкин"),
            new Author(2L, "Лермонтов"));

    @Test
    void listAllAuthors() throws Exception {
        mvc.perform(get("/authors/"))
                .andExpect(view().name("authorList"));

    }

    @Test
    void editPage() throws Exception {
        Author author = authors.get(0);
        when(authorService.findById(1L)).thenReturn(Optional.of(author));
        mvc.perform(get("/authors/edit/1"))
            .andExpect(view().name("authorEdit"))
            .andExpect(model().attribute("author", author));

    }

    @Test
    void shouldRenderErrorPageWhenAuthorNotFound() throws Exception {
        when(authorService.findById(1L)).thenThrow(new AuthorNotFoundException());
        mvc.perform(get("/authors/edit/1"))
                .andExpect(view().name("customError"));
    }
}