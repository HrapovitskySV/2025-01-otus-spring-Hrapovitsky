package ru.otus.hw.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Author;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Collections;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JdbcAuthorRepository implements AuthorRepository {
    private final NamedParameterJdbcOperations namedParameterJdbcOperations;


    @Override
    public List<Author> findAll() {

        return namedParameterJdbcOperations.query("select id, full_name from authors", new AuthorRowMapper());

    }

    @Override
    public Optional<Author> findById(long id) {
        Map<String, Object> params = Collections.singletonMap("id", id);

        List<Author> authors =  namedParameterJdbcOperations.query("select id, full_name from authors where id = :id",
                params, new AuthorRowMapper());

        if (authors.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.ofNullable(authors.get(0));
        }
    }

    private static class AuthorRowMapper implements RowMapper<Author> {

        @Override
        public Author mapRow(ResultSet rs, int i) throws SQLException {
            long authorId = rs.getLong("id");
            String authorFullName = rs.getString("full_name");
            return new Author(authorId, authorFullName);
        }
    }
}
