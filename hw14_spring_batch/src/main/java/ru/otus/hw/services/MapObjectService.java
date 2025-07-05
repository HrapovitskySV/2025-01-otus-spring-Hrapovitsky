package ru.otus.hw.services;

import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.modelsJpa.Author;
import ru.otus.hw.modelsJpa.Genre;
import ru.otus.hw.modelsJpa.Book;

import static java.util.Objects.isNull;

public interface MapObjectService {

   void putAuthor(String key, Author author);

   void convertMapAutorToMapIdAutor();

    void putGenre(String key, Genre genre);

    void convertMapGenreToMapIdGenre();

    void putBook(String key, Book book);

    void convertMapBookToMapIdBook();

    Long getIdAuthorFromKey(String key);

    Author getTemplateAuthorFromKey(String key);


    Long getIdGenreFromKey(String key);

    Genre getTemplateGenreFromKey(String key);

    Long getIdBookFromKey(String key);

}
