package ru.otus.hw.services;

import ru.otus.hw.modelsMongo.BookMongo;
import ru.otus.hw.modelsMongo.dto.BookMongoDto;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface BookService {
    Optional<BookMongo> findById(String id);

    List<BookMongoDto> findAll();

    BookMongo insert(String title, String authorId, Set<String> genresIDs);

    BookMongo update(String id, String title, String authorId, Set<String> genresIDs);

    void deleteById(String id);
}
