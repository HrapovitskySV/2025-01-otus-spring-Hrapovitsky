package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.converters.BookConverter;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.models.dto.BookDto;
import ru.otus.hw.repositories.BookRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.springframework.util.CollectionUtils.isEmpty;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    private final AuthorService authorService;

    private final GenreService genreService;

    private final CommentService commentService;

    private final BookConverter bookConverter;


    @Override
    @Transactional(readOnly = true)
    public Optional<Book> findById(String id) {
        return bookRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Book> findFirst() {
        Pageable firstPageWithTwoElements = PageRequest.of(0, 1);
        var books = bookRepository.findAll(firstPageWithTwoElements);
        return books.stream().findFirst();
    }


    @Override
    @Transactional(readOnly = true)
    public List<BookDto> findAll() {
        var books = bookRepository.findAll();
        return books.stream().map(bookConverter::toDto).toList();
    }

    @Override
    @Transactional
    public Book insert(String title, String authorName, Set<String> genreNames) {
        var genres = genreNames.stream().map(genreService::findByNameOrCreate).toList();
        Author author = authorService.findByNameOrCreate(authorName);
        return save(null, title, author, genres);
    }

    @Override
    @Transactional
    public Book update(String id, String title, String authorName, Set<String> genreNames) {
        var genres = genreNames.stream().map(genreService::findByNameOrCreate).toList();
        Author author = authorService.findByNameOrCreate(authorName);
        return save(id, title, author, genres);
    }

    @Override
    @Transactional
    public void deleteById(String bookId) {
        commentService.deleteByBookId(bookId);
        bookRepository.deleteById(bookId);

    }



    private Book save(String id, String title, Author author, List<Genre> genres) {
        if (isEmpty(genres)) {
            throw new IllegalArgumentException("Genres ids must not be null");
        }


        var book = new Book(id, title, author, genres.stream().toList());
        return bookRepository.save(book);
    }
}
