package ru.otus.hw.processors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.modelsJpa.Comment;
import ru.otus.hw.modelsMongo.CommentMongo;
import ru.otus.hw.services.CountMapper;

import java.util.Map;

import static java.util.Objects.isNull;

public class CommentItemProcessor implements ItemProcessor<CommentMongo, Comment> {

    private int lastId;

    private final Map<String, Integer> mapIdBook;


    public CommentItemProcessor(JdbcTemplate jdbcTemplate, Map<String, Integer> mapIdBook) {
        this.mapIdBook = mapIdBook;


        jdbcTemplate

                .query("SELECT max(id) as c FROM comments", new CountMapper())
                .forEach(count -> this.lastId = count);

    }


    public Comment process(final CommentMongo commentMongo) {
        lastId++;
        return new Comment(lastId,getMapId(mapIdBook,commentMongo.getBook().getId()), commentMongo.getComment());
    }

    public Integer getMapId(Map<String, Integer> mapId, String mongoId) {
        var id = mapId.get(mongoId);
        if ( isNull(id) ){
            throw new EntityNotFoundException("Not found SQL id book for MongoID "+mongoId);
        }
        return id;
    }
}