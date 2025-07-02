package ru.otus.hw.processors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.modelsJpa.Author;
import ru.otus.hw.modelsJpa.Book;
import ru.otus.hw.modelsJpa.Genre;
import ru.otus.hw.modelsMongo.BookMongo;
import ru.otus.hw.modelsMongo.GenreMongo;
import ru.otus.hw.services.CountMapper;

import java.util.HashMap;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;
import static java.util.stream.Collectors.toList;

public class BookItemProcessor implements ItemProcessor<BookMongo, Book> {

    private static final Logger log = LoggerFactory.getLogger(BookItemProcessor.class);

    private int lastId;

    private final HashMap<String, Integer> mapIdBook;

    private final HashMap<String, Integer> mapIdAuthor;

    private final HashMap<String, Integer> mapIdGenre;

    public BookItemProcessor(JdbcTemplate jdbcTemplate, HashMap<String, Integer> mapIdAuthor, HashMap<String, Integer> mapIdGenre, HashMap<String, Integer> mapIdBook) {
        this.mapIdAuthor = mapIdAuthor;
        this.mapIdGenre = mapIdGenre;
        this.mapIdBook = mapIdBook;

        jdbcTemplate
                .query("SELECT max(id) as c FROM books", new CountMapper())
                .forEach(count -> this.lastId = count);
    }


    public Book process(final BookMongo bookMongo) {
        lastId++;
        mapIdBook.put(bookMongo.getId(), lastId);
        var author = new Author(getMapId(mapIdAuthor,bookMongo.getAuthor().getId()),null);
        var genres = bookMongo.getGenres().stream()
                .map(genreMongo -> new Genre(getMapId(mapIdGenre,genreMongo.getId()),null)).toList();
        return new Book(lastId,bookMongo.getTitle(), author, genres);
    }

    public Integer getMapId(HashMap<String, Integer> mapId, String mongoId) {
        var id = mapId.get(mongoId);
        if ( isNull(id) ){
            throw new EntityNotFoundException("Not found SQL id for MongoID "+mongoId);
        }
        return id;
    }
}