package ru.otus.hw.services;

import ru.otus.hw.models.Book;
import ru.otus.hw.models.dto.BookDto;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface BookService {
    Optional<Book> findById(BigInteger id);

    List<BookDto> findAll();

    Book insert(String title, String authorName, Set<String> genreNames);

    Book update(BigInteger id, String title, String authorName, Set<String> genreNames);

    void deleteById(BigInteger id);
}
