package ru.otus.hw.services;

import ru.otus.hw.models.Book;
import ru.otus.hw.models.dto.BookDto;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface BookService {
    Optional<Book> findById(String id);

    Optional<Book> findFirst();

    List<BookDto> findAll();

    Book insert(String title, String authorName, Set<String> genreNames);

    Book update(String id, String title, String authorName, Set<String> genreNames);

    void deleteById(String id);
}
