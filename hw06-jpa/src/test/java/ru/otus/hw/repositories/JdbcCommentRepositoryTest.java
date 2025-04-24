package ru.otus.hw.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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

@DisplayName("Репозиторий на основе Jpa для работы с комментариями ")
@DataJpaTest
@Import({JdbcCommentRepository.class})
class JdbcCommentRepositoryTest {

    @Autowired
    private JdbcCommentRepository repositoryComment;

    private List<Book> dbBooks;

    private List<Comment> dbComments;

    @Autowired
    private TestEntityManager tem;


    @BeforeEach
    void setUp() {
        List<Author> dbAuthors = getDbAuthors();
        List<Genre> dbGenres = getDbGenres();
        dbBooks = getDbBooks(dbAuthors, dbGenres);

        dbComments = getDbComments(dbBooks);
        for (Comment comment: dbComments) {
            tem.persist(comment);
        }
    }


    @DisplayName("должен загружать комментарий по id")
    @Test
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
        return IntStream.range(1, 4).boxed()
                .map(id -> new Book(id,
                        "BookTitle_" + id,
                        dbAuthors.get(id - 1),
                        dbGenres.subList((id - 1) * 2, (id - 1) * 2 + 2)
                ))
                .toList();
    }

    private static List<Comment> getDbComments(List<Book> dbBooks) {
        return IntStream.range(1, 7).boxed()
                .map(id -> new Comment(0, dbBooks.get(Math.round((float) (id - 1) /2)), "Comment_" + id))
                .toList();
    }


}