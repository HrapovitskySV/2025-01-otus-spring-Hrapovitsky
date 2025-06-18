package ru.otus.hw.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.otus.hw.converters.AuthorConverter;
import ru.otus.hw.converters.BookConverter;
import ru.otus.hw.converters.GenreConverter;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.security.SecurityConfiguration;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.Objects.nonNull;
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


    @DisplayName("Should return expected status")
    @ParameterizedTest(name = "{0} {1} for user {2} should return {4} status")
    @MethodSource("getTestData")
    void shouldReturnExpectedStatus(String method, String url, String userName, String[] roles, int status, boolean checkLoginRedirection) throws Exception {

        var request = method2RequestBuilder(method, url);

        if (nonNull(userName)) {
            request = request.with((user(userName).roles(roles)));
        }


        ResultActions resultActions = mvc.perform(request)
                .andExpect(status().is(status));

        if (checkLoginRedirection) {
            resultActions.andExpect(redirectedUrlPattern("**/login"));
        }
    }

    private MockHttpServletRequestBuilder method2RequestBuilder(String method, String url) {
        Map<String, Function<String, MockHttpServletRequestBuilder>> methodMap =
                Map.of("get", MockMvcRequestBuilders::get,
                        "post", MockMvcRequestBuilders::post,
                        "put", MockMvcRequestBuilders::put,
                        "delete", MockMvcRequestBuilders::delete);

        return  methodMap.get(method).apply(url);
    }

    public static Stream<Arguments> getTestData() throws JsonProcessingException {
        var roles = new String[] {"USER"};


        return Stream.of(
                Arguments.of("get", "/api/books", null, null,302, true),
                Arguments.of("get", "/api/books/1", null, null,302, true),
                Arguments.of("get", "/api/authors/", null, null,302, true),
                Arguments.of("get", "/api/authors/1", null, null,302, true),

                Arguments.of("post", "/api/authors", null, null,302, true),
                Arguments.of("post", "/api/authors", "USER", roles,400, false),
                Arguments.of("put", "/api/authors", null, null,302, true),
                Arguments.of("put", "/api/authors", "USER", roles,400, false),
                Arguments.of("delete", "/api/authors/1", null, null,302, true),
                Arguments.of("delete", "/api/authors/1", "USER", roles,200, false),


                Arguments.of("post", "/api/books", null, null,302, true),
                Arguments.of("post", "/api/books", "USER", roles,400, false),
                Arguments.of("put", "/api/books", null, null,302, true),
                Arguments.of("put", "/api/books", "USER", roles,400, false),
                Arguments.of("delete", "/api/books/1", null, null,302, true),
                Arguments.of("delete", "/api/books/1", "USER", roles,200, false),


                Arguments.of("get", "/api/books", "USER", roles,200, false),
                Arguments.of("get", "/api/books/1", "USER", roles,200, false),
                Arguments.of("get", "/api/authors", "USER", roles,200, false),
                Arguments.of("get", "/api/authors/1", "USER", roles,200, false)

        );
    }



}