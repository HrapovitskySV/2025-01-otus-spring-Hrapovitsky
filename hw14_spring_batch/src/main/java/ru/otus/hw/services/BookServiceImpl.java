package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.converters.BookConverter;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.modelsMongo.AuthorMongo;
import ru.otus.hw.modelsMongo.BookMongo;
import ru.otus.hw.modelsMongo.GenreMongo;
import ru.otus.hw.modelsMongo.dto.BookMongoDto;
import ru.otus.hw.repositoriesMongo.BookMongoRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.springframework.util.CollectionUtils.isEmpty;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {

    private final BookMongoRepository bookRepository;

    private final AuthorService authorService;

    private final GenreService genreService;

    private final CommentService commentService;

    private final BookConverter bookConverter;


    @Override
    @Transactional(readOnly = true)
    public Optional<BookMongo> findById(String id) {
        return bookRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookMongoDto> findAll() {
        var books = bookRepository.findAll();
        return books.stream().map(bookConverter::toDto).toList();
    }

    @Override
    @Transactional
    public BookMongo insert(String title, String authorId, Set<String> genresId) {
        return save(null, title, authorId, genresId);
    }

    @Override
    @Transactional
    public BookMongo update(String id, String title, String authorId, Set<String> genresId) {
        return save(id, title, authorId, genresId);
    }

    @Override
    @Transactional
    public void deleteById(String bookId) {
        commentService.deleteByBookId(bookId);
        bookRepository.deleteById(bookId);

    }

    @Transactional
    public BookMongo save(String id, String title, String authorId, Set<String> genresId) {
        var genres = genresId.stream()
                .map(genreService::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
        if (isEmpty(genresId) || genresId.size() != genres.size()) {
            throw new EntityNotFoundException("One or all genres with ids %s not found".formatted(genresId));
        }
        AuthorMongo author = authorService.findById(authorId)
                .orElseThrow(() -> new EntityNotFoundException("Author with id %s not found".formatted(authorId)));
        return save(id, title, author, genres);
    }



    private BookMongo save(String id, String title, AuthorMongo author, List<GenreMongo> genres) {
        if (isEmpty(genres)) {
            throw new IllegalArgumentException("Genres ids must not be null");
        }


        var book = new BookMongo(id, title, author, genres.stream().toList());
        return bookRepository.save(book);
    }
}
