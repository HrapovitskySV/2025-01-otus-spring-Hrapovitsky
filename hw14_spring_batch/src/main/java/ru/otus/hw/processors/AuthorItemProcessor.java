package ru.otus.hw.processors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.otus.hw.modelsMongo.AuthorMongo;
import ru.otus.hw.modelsJpa.Author;
import ru.otus.hw.services.CountMapper;

import java.util.HashMap;

public class AuthorItemProcessor implements ItemProcessor<AuthorMongo, Author> {

    private static final Logger log = LoggerFactory.getLogger(AuthorItemProcessor.class);

    private int lastId;
    private final HashMap<String, Integer> mapId;

    public AuthorItemProcessor(JdbcTemplate jdbcTemplate, HashMap<String, Integer> mapId) {
        this.mapId = mapId;

        jdbcTemplate
                .query("SELECT max(id) as c FROM authors", new CountMapper())
                .forEach(count -> this.lastId = count);
    }


    @Override
    public Author process(final AuthorMongo authorMongo) {
        lastId++;
        mapId.put(authorMongo.getId(), lastId);
        return new Author(lastId,authorMongo.getFullName());
    }
}