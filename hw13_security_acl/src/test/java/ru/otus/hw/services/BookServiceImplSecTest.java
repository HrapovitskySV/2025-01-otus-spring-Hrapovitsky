package ru.otus.hw.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.converters.AuthorConverter;
import ru.otus.hw.converters.BookConverter;
import ru.otus.hw.converters.GenreConverter;
import ru.otus.hw.security.AclConfig;
import ru.otus.hw.security.AclMethodSecurityConfiguration;

import org.springframework.security.access.AccessDeniedException;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Сервис для работы с книгами безопасность")
@DataJpaTest()
@Import({BookServiceImpl.class, BookConverter.class, AuthorConverter.class, GenreConverter.class, AclConfig.class, AclMethodSecurityConfiguration.class})//, AclServiceWrapperService.class
@Transactional(propagation = Propagation.NEVER)
class BookServiceImplSecTest {

    @Autowired
    private BookService bookService;

    @MockBean
    private AclServiceWrapperService aclServiceWrapperService;


    @DisplayName("Пользователь USER не видит книгу с ИД 1")
    @WithMockUser(username="USER",roles={"USER"})
    @Test
    void findById1() {
        assertThrows(AccessDeniedException.class, () -> {
            bookService.findById(1L);
        });
    }

    @DisplayName("Пользователь USER видит книгу с ИД 2")
    @WithMockUser(username="USER",roles={"USER"})
    @Test
    void findById2() {
        assertDoesNotThrow(() -> {
            bookService.findById(2L);
        });
    }

    @DisplayName("Под пользователем USER видно только 1 книгу")
    @WithMockUser(username="USER",roles={"USER"})
    @Test
    void findAll() {
        var actualBooks = bookService.findAll();
        assertEquals(actualBooks.size(),1);
    }

}