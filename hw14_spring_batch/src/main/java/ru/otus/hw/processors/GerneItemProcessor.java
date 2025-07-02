package ru.otus.hw.processors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.otus.hw.modelsJpa.Genre;
import ru.otus.hw.modelsMongo.GenreMongo;
import ru.otus.hw.services.CountMapper;

import java.util.HashMap;
import java.util.Map;

public class GerneItemProcessor implements ItemProcessor<GenreMongo, Genre> {

    private static final Logger log = LoggerFactory.getLogger(GerneItemProcessor.class);

    private int lastId;
    private final Map<String, Integer> mapId;

    public GerneItemProcessor(JdbcTemplate jdbcTemplate, Map<String, Integer> mapId) {
        this.mapId = mapId;

        jdbcTemplate
                .query("SELECT max(id) as c FROM genres", new CountMapper())
                .forEach(count -> this.lastId = count);
    }

    @Override
    public Genre process(final GenreMongo genreMongo) {
        lastId++;
        mapId.put(genreMongo.getId(), lastId);
        return new Genre(lastId,genreMongo.getName());
    }
}