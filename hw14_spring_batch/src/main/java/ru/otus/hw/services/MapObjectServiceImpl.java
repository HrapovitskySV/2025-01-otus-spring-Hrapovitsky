package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.modelsJpa.Author;
import ru.otus.hw.modelsJpa.Book;
import ru.otus.hw.modelsJpa.Genre;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static java.util.Objects.isNull;

@RequiredArgsConstructor
@Service
public class MapObjectServiceImpl implements MapObjectService {
    private final Map<String, Long> mapIdAuthor = new ConcurrentHashMap<String, Long>();

    private final Map<String, Author> mapAuthors = new ConcurrentHashMap<String, Author>();

    private final Map<String, Genre> mapGenres = new ConcurrentHashMap<String, Genre>();

    private final Map<String, Long> mapIdGenre = new ConcurrentHashMap<String, Long>();

    private final Map<String, Book> mapBooks = new ConcurrentHashMap<String, Book>();

    private final Map<String, Long> mapIdBook = new ConcurrentHashMap<String, Long>();

    @Override
    public void putAuthor(String key, Author author) {
        mapAuthors.put(key, author);
    }

    public Boolean convertMapAutorToMapIdAutor(){
        mapAuthors.forEach((key, author) -> mapIdAuthor.put(key, author.getId()));
        mapAuthors.clear();
        return true;
    }

    @Override
    public void putGenre(String key, Genre genre) {
        mapGenres.put(key, genre);
    }

    @Override
    public Boolean convertMapGenreToMapIdGenre() {
        mapGenres.forEach((key, genre) -> mapIdGenre.put(key, genre.getId()));
        mapGenres.clear();
        return true;
    }

    @Override
    public void putBook(String key, Book book) {
        mapBooks.put(key, book);
    }

    @Override
    public Boolean convertMapBookToMapIdBook() {
        mapBooks.forEach((key, book) -> mapIdBook.put(key, book.getId()));
        mapBooks.clear();
        return true;
    }

    public Long getIdAuthorFromKey(String key) {
        var id = mapIdAuthor.get(key);
        if (isNull(id)) {
            throw new EntityNotFoundException("Not found SQL id  author for MongoID " + key);
        }
        return id;
    }

    @Override
    public Author getTemplateAuthorFromKey(String key) {
        return new Author(getIdAuthorFromKey(key), null);
    }

    public Long getIdGenreFromKey(String key) {
        var id = mapIdGenre.get(key);
        if (isNull(id)) {
            throw new EntityNotFoundException("Not found SQL id genre for MongoID " + key);
        }
        return id;
    }

    @Override
    public Genre getTemplateGenreFromKey(String key) {
        return new Genre(getIdGenreFromKey(key), null);
    }

    public Long getIdBookFromKey(String key) {
        var id = mapIdBook.get(key);
        if (isNull(id)) {
            throw new EntityNotFoundException("Not found SQL id book for MongoID " + key);
        }
        return id;
    }

    @Override
    public Book getTemplateBookFromKey(String key) {
        return new Book(getIdBookFromKey(key), null, null, List.of());
    }

}
