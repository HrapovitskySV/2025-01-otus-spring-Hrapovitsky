package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.converters.BookConverter;
import ru.otus.hw.exceptions.EntityNotFoundException;
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
    public List<BookDto> findAll() {
        var books = bookRepository.findAll();
        return books.stream().map(bookConverter::toDto).toList();
    }

    @Override
    @Transactional
    public Book insert(String title, String authorId, Set<String> genresId) {
        return save(null, title, authorId, genresId);
    }

    @Override
    @Transactional
    public Book update(String id, String title, String authorId, Set<String> genresId) {
        return save(id, title, authorId, genresId);
    }

    @Override
    @Transactional
    public void deleteById(String bookId) {
        commentService.deleteByBookId(bookId);
        bookRepository.deleteById(bookId);

    }

    @Transactional
    public Book save(String id, String title, String authorId, Set<String> genresId) {
        var genres = genresId.stream().map(genreService::findById).filter(Optional::isPresent).map(Optional::get).toList();
        if (isEmpty(genresId) || genresId.size() != genres.size()) {
            throw new EntityNotFoundException("One or all genres with ids %s not found".formatted(genresId));
        }
        Author author = authorService.findById(authorId).orElseThrow(() -> new EntityNotFoundException("Author with id %s not found".formatted(authorId)));
        return save(id, title, author, genres);
    }



    private Book save(String id, String title, Author author, List<Genre> genres) {
        if (isEmpty(genres)) {
            throw new IllegalArgumentException("Genres ids must not be null");
        }


        var book = new Book(id, title, author, genres.stream().toList());
        return bookRepository.save(book);
    }
}
