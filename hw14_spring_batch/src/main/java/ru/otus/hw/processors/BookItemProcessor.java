package ru.otus.hw.processors;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.modelsJpa.Author;
import ru.otus.hw.modelsJpa.Book;
import ru.otus.hw.modelsJpa.Genre;
import ru.otus.hw.modelsMongo.BookMongo;
import ru.otus.hw.services.CountMapper;
import ru.otus.hw.services.MapObjectService;

import java.util.Map;

import static java.util.Objects.isNull;

@RequiredArgsConstructor
public class BookItemProcessor implements ItemProcessor<BookMongo, Book> {

    private final MapObjectService mapObjectService;
    /*
    private int lastId;

    private final Map<String, Integer> mapIdBook;

    private final Map<String, Integer> mapIdAuthor;

    private final Map<String, Integer> mapIdGenre;

    public BookItemProcessor(JdbcTemplate jdbcTemplate,
                             Map<String, Integer> mapIdAuthor,
                             Map<String, Integer> mapIdGenre,
                             Map<String, Integer> mapIdBook) {

        this.mapIdAuthor = mapIdAuthor;
        this.mapIdGenre = mapIdGenre;
        this.mapIdBook = mapIdBook;

        jdbcTemplate
                .query("SELECT max(id) as c FROM books", new CountMapper())
                .forEach(count -> this.lastId = count);
    }
    */



    public Book process(final BookMongo bookMongo) {
        //lastId++;
        //mapIdBook.put(bookMongo.getId(), lastId);
        var author = mapObjectService.getTemplateAuthorFromKey(bookMongo.getAuthor().getId());
        var genres = bookMongo.getGenres().stream()
                .map(genreMongo -> mapObjectService.getTemplateGenreFromKey(genreMongo.getId())).toList();

        var book = new Book(0,bookMongo.getTitle(), author, genres);
        mapObjectService.putBook(bookMongo.getId(), book);
        return book;
    }
}