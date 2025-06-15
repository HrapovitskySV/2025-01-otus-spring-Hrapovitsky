package ru.otus.hw.rest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.security.SecurityConfiguration;
import ru.otus.hw.services.AuthorService;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = AuthorRestController.class)
@Import({SecurityConfiguration.class})  // включаю настройки Security
class AuthorRestControllerSecTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private AuthorService authorService;


    @Test
    void testAuthenticatedOnUser() throws Exception {
        mvc.perform(get("/api/authors").with(user("USER").roles("USER")))
                .andExpect(status().isOk());
        mvc.perform(get("/api/authors/1").with(user("USER").roles("USER")))
                .andExpect(status().isOk());
    }

    @Test
    void testAuthorListWithoutAuth() throws Exception {
        mvc.perform(get("/api/authors"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrlPattern("**/login"));
    }


}