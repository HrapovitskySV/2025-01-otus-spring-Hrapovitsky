package ru.otus.hw.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.CustomUser;
import ru.otus.hw.models.Role;
import ru.otus.hw.repositories.CustomUserRepository;
import ru.otus.hw.repositories.RoleRepository;
import ru.otus.hw.security.SecurityConfiguration;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.CustomUserDetailsService;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthorRestController.class)
@Import({SecurityConfiguration.class})
class AuthorRestControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

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
    void listAllAuthors() throws Exception {
        when(authorService.findAll()).thenReturn(authors);
        mvc.perform(get("/api/authors"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(authors)));
    }

    @Test
    void getAuthorTest() throws Exception {
        var author = authors.get(0);
        when(authorService.findById(1L)).thenReturn(Optional.of(author));
        mvc.perform(get("/api/authors/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(author)));
    }


    @Test
    void shouldCorrectSaveNewAuthor() throws Exception {
        Author author = new Author(3, "Author3");
        given(authorService.save(any())).willReturn(author);
        String expectedResult = mapper.writeValueAsString(author);

        mvc.perform(post("/api/authors").contentType(APPLICATION_JSON)
                        .content(expectedResult))
                .andExpect(status().isCreated())
                .andExpect(content().json(expectedResult));

    }


}