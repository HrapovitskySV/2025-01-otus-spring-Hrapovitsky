package ru.otus.hw.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Jpa для работы с книгами ")
@DataJpaTest
@Import({JdbcBookRepository.class, JdbcGenreRepository.class, JdbcCommentRepository.class})
class JdbcBookRepositoryTest {

    @Autowired
    private JdbcBookRepository repositoryJdbc;

    private List<Author> dbAuthors;

    private List<Genre> dbGenres;

    private List<Book> dbBooks;

    @Autowired
    private JdbcCommentRepository repositoryComment;

    private List<Comment> dbComments;

    @Autowired
    TestEntityManager tem;


    @BeforeEach
    void setUp() {
        dbAuthors = getDbAuthors();
        dbGenres = getDbGenres();
        dbBooks = getDbBooks(dbAuthors, dbGenres);
        for (Book book: dbBooks) {
            repositoryJdbc.save(book);
        }
        dbComments = getDbComments(dbBooks);
        for (Comment comment: dbComments) {
            repositoryComment.save(comment);
        }
    }

    @DisplayName("должен загружать книгу по id")
    @ParameterizedTest
    @MethodSource("getDbBooks")
    void shouldReturnCorrectBookById(Book expectedBook) {
        var actualBook = repositoryJdbc.findById(expectedBook.getId());
        assertThat(actualBook).isPresent()
                .get()
                .isEqualTo(expectedBook);
    }

    @DisplayName("должен загружать список всех книг")
    @Test
    void shouldReturnCorrectBooksList() {
        var actualBooks = repositoryJdbc.findAll();
        var expectedBooks = dbBooks;

        assertThat(actualBooks).containsExactlyElementsOf(expectedBooks);
        actualBooks.forEach(System.out::println);
    }

    @DisplayName("должен сохранять новую книгу")
    @Test
    void shouldSaveNewBook() {
        var expectedBook = new Book(0, "BookTitle_10500", dbAuthors.get(0),
                List.of(dbGenres.get(0), dbGenres.get(2)));
        var returnedBook = repositoryJdbc.save(expectedBook);
        assertThat(returnedBook).isNotNull()
                .matches(book -> book.getId() > 0)
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedBook);


        var actualBook = Optional.ofNullable(tem.find(Book.class, returnedBook.getId()));

        assertThat(actualBook)
                .isPresent()
                .get()
                .isEqualTo(returnedBook);
    }

    @DisplayName("должен сохранять измененную книгу")
    @Test
    void shouldSaveUpdatedBook() {
        var expectedBook = new Book(1L, "BookTitle_10500", dbAuthors.get(2),
                List.of(dbGenres.get(4), dbGenres.get(5)));

        var actualBook = Optional.ofNullable(tem.find(Book.class, expectedBook.getId()));

        assertThat(actualBook)
                .isPresent()
                .get()
                .isNotEqualTo(expectedBook);

        var returnedBook = repositoryJdbc.save(expectedBook);
        assertThat(returnedBook).isNotNull()
                .matches(book -> book.getId() > 0)
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedBook);

        actualBook = Optional.ofNullable(tem.find(Book.class, returnedBook.getId()));
        assertThat(actualBook)
                .isPresent()
                .get()
                .isEqualTo(returnedBook);
    }

    @DisplayName("должен удалять книгу по id ")
    @Test
    void shouldDeleteBook() {
        var actualBook = Optional.ofNullable(tem.find(Book.class, 1L));
        assertThat(actualBook).isPresent();

        repositoryJdbc.deleteById(1L);

        actualBook = Optional.ofNullable(tem.find(Book.class, 1L));
        assertThat(actualBook).isEmpty();
    }


    @DisplayName("должен загружать комментарий по id")
    @Test
    //@ParameterizedTest
    //@MethodSource("getDbComments")
    //void shouldReturnCorrectCommentById(Comment expectedComment) {
    void shouldReturnCorrectCommentById() {
        for (Comment expectedComment: dbComments) {
            var actualComment = repositoryComment.findById(expectedComment.getId());
            assertThat(actualComment).isPresent()
                    .get()
                    .isEqualTo(expectedComment);
        }
    }

    @DisplayName("должен сохранять новый комментарий")
    @Test
    void shouldSaveNewComment() {
        var expectedComment = new Comment(0, dbBooks.get(0), "Comment_10501");
        var returnedComment = repositoryComment.save(expectedComment);
        assertThat(returnedComment).isNotNull()
                .matches(comment -> comment.getId() > 0)
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedComment);

        var actualComment = Optional.ofNullable(tem.find(Comment.class, returnedComment.getId()));

        assertThat(actualComment)
                .isPresent()
                .get()
                .isEqualTo(returnedComment);
    }

    @DisplayName("должен сохранять измененный комментари")
    @Test
    void shouldSaveUpdatedComment() {
        long id = dbComments.get(0).getId();

        var expectedComment = new Comment(id, dbBooks.get(0), "Comment_10500");

        var actualComment = Optional.ofNullable(tem.find(Comment.class, id));

        assertThat(actualComment)
                .isPresent()
                .get()
                .isNotEqualTo(expectedComment);

        var returnedComment = repositoryComment.save(expectedComment);
        assertThat(returnedComment).isNotNull()
                .matches(comment -> comment.getId() > 0)
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedComment);


        actualComment = Optional.ofNullable(tem.find(Comment.class, id));
        assertThat(actualComment)
                .isPresent()
                .get()
                .isEqualTo(returnedComment);
    }

    @DisplayName("должен удалять комментарий по id ")
    @Test
    void shouldDeleteComment() {
        long id = dbComments.get(0).getId();
        var actualComment = Optional.ofNullable(tem.find(Comment.class, id));
        assertThat(actualComment).isPresent();

        repositoryComment.deleteById(id);

        actualComment = Optional.ofNullable(tem.find(Comment.class, id));
        assertThat(actualComment).isEmpty();
    }

    private static List<Author> getDbAuthors() {
        return IntStream.range(1, 4).boxed()
                .map(id -> new Author(id, "Author_" + id))
                .toList();
    }

    private static List<Genre> getDbGenres() {
        return IntStream.range(1, 7).boxed()
                .map(id -> new Genre(id, "Genre_" + id))
                .toList();
    }

    private static List<Book> getDbBooks(List<Author> dbAuthors, List<Genre> dbGenres) {
        List<Book> books= IntStream.range(1, 4).boxed()
                .map(id -> new Book(id,
                        "BookTiiiiitle_" + id,
                        dbAuthors.get(id - 1),
                        dbGenres.subList((id - 1) * 2, (id - 1) * 2 + 2)
                ))
                .toList();


        return books;
    }

    private static List<Comment> getDbComments(List<Book> dbBooks) {
        return IntStream.range(1, 7).boxed()
                .map(id -> new Comment(0, dbBooks.get(Math.round((id-1)/2)), "Comment_" + id))
                .toList();
    }

    private static List<Book> getDbBooks() {
        var dbAuthors = getDbAuthors();
        var dbGenres = getDbGenres();
        return getDbBooks(dbAuthors, dbGenres);
    }

    private static List<Comment> getDbComments() {
        var dbBooks = getDbBooks();
        return getDbComments(dbBooks);
    }
}