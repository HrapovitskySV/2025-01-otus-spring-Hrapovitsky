package ru.otus.hw.repositories;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
public class JdbcGenreRepository implements GenreRepository {



    private final NamedParameterJdbcOperations namedParameterJdbcOperations;

    public JdbcGenreRepository(NamedParameterJdbcOperations namedParameterJdbcOperations) {
        this.namedParameterJdbcOperations = namedParameterJdbcOperations;
    }

    @Override
    public List<Genre> findAll() {
        return namedParameterJdbcOperations.query("select id, name from genres", new GnreRowMapper());
    }
    @Override
    public List<Genre> findAllByIds(Set<Long> ids) {
        Map<String, Object> params = Collections.singletonMap("ids", ids);

        return  namedParameterJdbcOperations.query("select id, name from genres where id in (:ids)",
                params, new GnreRowMapper());
    }

    public Optional<Genre> findById(long id) {
        Map<String, Object> params = Collections.singletonMap("id", id);

        List<Genre> genres =  namedParameterJdbcOperations.query("select id, name from genres where id = :id",
                params, new GnreRowMapper());

        if (genres.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.ofNullable(genres.get(0));
        }
    }


    private static class GnreRowMapper implements RowMapper<Genre> {

        public Genre mapRow(ResultSet rs, int i) throws SQLException {
            long id = rs.getLong("id");
            String name = rs.getString("name");
            return new Genre(id, name);
        }
    }
}
