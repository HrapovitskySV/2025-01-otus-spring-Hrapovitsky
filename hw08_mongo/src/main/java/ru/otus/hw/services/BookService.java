package ru.otus.hw.services;

import ru.otus.hw.models.Book;
import ru.otus.hw.models.dto.BookDto;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface BookService {
    Optional<Book> findById(String id);

    List<BookDto> findAll();

    Book insert(String title, String authorId, Set<String> genresIDs);

    Book update(String id, String title, String authorId, Set<String> genresIDs);

    void deleteById(String id);
}
