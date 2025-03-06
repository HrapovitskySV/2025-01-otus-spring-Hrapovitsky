package ru.otus.hw.repositories;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Book;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcBookRepository implements BookRepository {

    @Override
    public Optional<Book> findById(long id) {


        return Optional.empty();

    }

    @Override
    public List<Book> findAll() {

        return jdbc.query("select b.id, b.title, b.author_id, b.genre_id, a.full_name as author_name, g.name as genre_name" +
                "from books as b " +
                "   left join authors as a " +
                "       on b.author_id=a.id " +
                "   left join genres as g " +
                "       on b.genre_id=g.id", new BookRowMapper());
        return new ArrayList<>();
    }

    @Override
    public Book save(Book book) {
        if (book.getId() == 0) {
            return insert(book);
        }
        return update(book);
    }

    @Override
    public void deleteById(long id) {
        //...
    }

    private Book insert(Book book) {
        var keyHolder = new GeneratedKeyHolder();

        //...

        //noinspection DataFlowIssue
        book.setId(keyHolder.getKeyAs(Long.class));
        return book;
    }

    private Book update(Book book) {
        //...
        // Выбросить EntityNotFoundException если не обновлено ни одной записи в БД
        return book;
    }

    private static class BookRowMapper implements RowMapper<Book> {

        @Override
        public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
            //return jdbc.query("select b.id, b.title, b.author_id, b.genre_id, a.full_name as author_name, g.name as genre_name" +

            long author_id = resultSet.getLong("author_id");
            Author author;
            if (!Objects.isNull(author_id)){
                author=new Author(author_id, resultSet.getLong("author_name"));
            }
            long genre_id = resultSet.getLong("genre_id");
            Genre genre;
            if (!Objects.isNull(genre_id)){
                genre=new Genre(genre_id, resultSet.getLong("genre_name"));
            }

            long id = resultSet.getLong("id");
            String title = resultSet.getString("title");
            return new Book(id, title, author, genre);

        }
    }
}
