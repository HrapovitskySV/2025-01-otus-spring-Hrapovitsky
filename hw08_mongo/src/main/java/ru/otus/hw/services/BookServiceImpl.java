package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.converters.BookConverter;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.models.dto.BookDto;
import ru.otus.hw.repositories.BookRepository;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import static org.springframework.util.CollectionUtils.isEmpty;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    private final CommentService commentService;

    private final BookConverter bookConverter;

    @Override
    @Transactional
    public Optional<Book> findById(BigInteger id) {
        return bookRepository.findById(id);
    }

    @Override
    @Transactional
    public List<BookDto> findAll() {
        var books = bookRepository.findAll();
        return books.stream().map(bookConverter::toDto).toList();

    }



    @Override
    public Book insert(String title, String authorName, Set<String> genreNames) {
        AtomicInteger index = new AtomicInteger();
        var genres = genreNames.stream().map(genreName -> new Genre(index.getAndIncrement(),genreName)).toList();
        return save(null, title, new Author(1, authorName), genres);
    }

    @Override
    public Book update(BigInteger id, String title, String authorName, Set<String> genreNames) {
        AtomicInteger index = new AtomicInteger();
        var genres = genreNames.stream().map(genreName -> new Genre(index.getAndIncrement(),genreName)).toList();

        return save(id, title, new Author(1, authorName), genres);
    }

    @Override
    @Transactional
    public void deleteById(BigInteger bookId) {
        commentService.deleteByBookId(bookId);
        bookRepository.deleteById(bookId);

    }

    private Book save(BigInteger id, String title, Author author, List<Genre> genres) {
        if (isEmpty(genres)) {
            throw new IllegalArgumentException("Genres ids must not be null");
        }


        var book = new Book(id, title, author, genres.stream().toList());
        return bookRepository.save(book);
    }
}
