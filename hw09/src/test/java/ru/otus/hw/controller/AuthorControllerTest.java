package ru.otus.hw.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.models.Author;
import ru.otus.hw.services.AuthorService;

import java.util.List;
import java.util.Optional;


import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthorController.class)
class AuthorControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private AuthorService authorService;

    private final List<Author> authors = List.of(new Author(1L, "Пушкин"),
            new Author(2L, "Лермонтов"));

    @Test
    void listAllAuthors() throws Exception {
        when(authorService.findAll()).thenReturn(authors);
        mvc.perform(get("/authors/"))
                .andExpect(view().name("authorList"))
                .andExpect(model().attribute("authors", authors));
    }

    @Test
    void editPage() throws Exception {
        Author author = authors.get(0);
        when(authorService.findById(1L)).thenReturn(Optional.of(author));
        mvc.perform(get("/authors/edit").param("id", "1"))
            .andExpect(view().name("authorEdit"))
            .andExpect(model().attribute("author", author));

    }

    @Test
    void shouldRenderErrorPageWhenAuthorNotFound() throws Exception {
        when(authorService.findById(1L)).thenThrow(new AuthorNotFoundException());
        mvc.perform(get("/authors/edit").param("id", "1"))
                .andExpect(view().name("customError"));
    }

    @Test
    void shouldSaveAuthorAndRedirectToContextPath() throws Exception {
        when(authorService.findById(1L)).thenReturn(Optional.of(authors.get(0)));
        mvc.perform(post("/authors/edit").param("id", "3").param("fullName", "Толстой"))
                .andExpect(view().name("redirect:/authors/"));
        verify(authorService, times(1)).save(any(Author.class));
    }

}