package ru.otus.hw.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class JdbcBookRepository implements BookRepository {

    private final NamedParameterJdbcOperations namedParameterJdbcOperations;

    private final GenreRepository genreRepository;


    @Override
    public Optional<Book> findById(long id) {
        Map<String, Object> params = Collections.singletonMap("id", id);

        return Optional.ofNullable(namedParameterJdbcOperations.query("select " +
                "b.id, b.title, b.author_id as author_id, bg.genre_id," +
                "a.full_name as author_full_name, g.name as genre_name " +
                "from books as b" +
                "   left join authors as a on b.author_id=a.id" +
                "   left join books_genres as bg " +
                "    left join genres as g " +
                "       on bg.genre_id=g.id" +
                "   on b.id=bg.book_id" +
                " where b.id = :id", params, new BookResultSetExtractor()));
    }

    @Override
    public List<Book> findAll() {
        var genres = genreRepository.findAll();
        var relations = getAllGenreRelations();
        var books = getAllBooksWithoutGenres();
        mergeBooksInfo(books, genres, relations);
        return books;
    }

    @Override
    public Book save(Book book) {
        if (book.getId() == 0) {
            return insert(book);
        }
        return update(book);
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        Map<String, Object> params = Collections.singletonMap("id", id);
        namedParameterJdbcOperations.update(
                "delete from books where id = :id", params
        );

        namedParameterJdbcOperations.update(
                "delete from books_genres where book_id  = :id", params
        );
    }

    private List<Book> getAllBooksWithoutGenres() {
        return namedParameterJdbcOperations.query("select " +
                "b.id, b.title, b.author_id, a.full_name as author_full_name " +
                "from books as b" +
                "   left join authors as a on b.author_id=a.id", new BookRowMapper());

    }

    private List<BookGenreRelation> getAllGenreRelations() {

        return namedParameterJdbcOperations.query(
                "select book_id, genre_id from books_genres"
                , Map.of(), new BookGenreRelationRowMapper()
        );

    }

    private void mergeBooksInfo(List<Book> booksWithoutGenres, List<Genre> genres,
                                List<BookGenreRelation> relations) {
        // Добавить книгам (booksWithoutGenres) жанры (genres) в соответствии со связями (relations)

        Map<Long, Book> mapBooks = booksWithoutGenres.stream()
                .collect(Collectors.toMap(Book::getId, book -> book));
        Map<Long, Genre> mapGenres = genres.stream()
                .collect(Collectors.toMap(Genre::getId, genre -> genre));

        for (BookGenreRelation bookGenreRelation:relations) {
            Book book = mapBooks.get(bookGenreRelation.bookId);
            Genre genre = mapGenres.get(bookGenreRelation.genreId);

            book.addGenre(genre);

        }
    }

    @Transactional
    private Book insert(Book book) {
        var keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("title", book.getTitle());
        params.addValue("author_id", book.getAuthor().getId());

        namedParameterJdbcOperations.update("insert into books ( title, author_id) values (:title, :author_id)",
                params, keyHolder, new String[]{"id"});

        book.setId(keyHolder.getKeyAs(Long.class));
        batchInsertGenresRelationsFor(book);
        return book;
    }

    @Transactional
    private Book update(Book book) {
        if (Objects.isNull(findById(book.getId()))) {
            throw new EntityNotFoundException(String.format("Book withs id %s not exists",findById(book.getId())));
        } else {
            MapSqlParameterSource params = new MapSqlParameterSource();
            params.addValue("id", book.getId());
            params.addValue("title", book.getTitle());
            params.addValue("author_id", book.getAuthor().getId());

            namedParameterJdbcOperations.update("update books " +
                    "set title=:title, author_id=:author_id " +
                    "where id = :id",params);
        }

        removeGenresRelationsFor(book);
        batchInsertGenresRelationsFor(book);

        return book;
    }

    private void batchInsertGenresRelationsFor(Book book) {
        SqlParameterSource[] batchVales = book.getGenres().stream()
                .map(genre -> new MapSqlParameterSource()
                        .addValue("book_id", book.getId())
                        .addValue("genre_id", genre.getId()))
                .toArray(SqlParameterSource[]::new);


        namedParameterJdbcOperations.batchUpdate(
                "insert into books_genres ( book_id, genre_id) values(:book_id,:genre_id)",
                batchVales);

    }

    private void removeGenresRelationsFor(Book book) {
        Map<String, Object> params = Collections.singletonMap("id", book.getId());

        namedParameterJdbcOperations.update(
                "delete from books_genres where book_id  = :id", params
        );
    }

    private static class BookRowMapper implements RowMapper<Book> {

        @Override
        public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
            List<Genre> genres = new ArrayList<>();

            Author author = null;
            long authorId = rs.getLong("author_id");
            if (authorId != 0) {
                String authorFullName = rs.getString("author_full_name");
                author = new Author(authorId, authorFullName);
            }

            long id = rs.getLong("id");
            String title = rs.getString("title");
            return new Book(id, title, author,genres);
        }
    }

    // Использовать для findById
    @SuppressWarnings("ClassCanBeRecord")
    @RequiredArgsConstructor
    private static class BookResultSetExtractor implements ResultSetExtractor<Book> {

        @Override
        public Book extractData(ResultSet rs) throws SQLException, DataAccessException {
            Book book = null;
            List<Genre> genres = new ArrayList<Genre>();

            while (rs.next()) {
                if (rs.isFirst()) {
                    book = new Book();
                    book.setId(rs.getLong("id"));
                    book.setTitle(rs.getString("title"));
                    book.setGenres(genres);

                    long authorId = rs.getLong("author_id");
                    if (authorId != 0) {
                        String authorFullName = rs.getString("author_full_name");
                        book.setAuthor(new Author(authorId, authorFullName));
                    }
                }
                genres.add(new Genre(rs.getLong("genre_id"), rs.getString("genre_name")));
            }
            return book;
        }
    }


    private record BookGenreRelation(long bookId, long genreId) {

    }

    private static class BookGenreRelationRowMapper implements RowMapper<BookGenreRelation> {

        @Override
        public BookGenreRelation mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new BookGenreRelation(rs.getLong("book_id"), rs.getLong("genre_id"));
        }

    }


}

