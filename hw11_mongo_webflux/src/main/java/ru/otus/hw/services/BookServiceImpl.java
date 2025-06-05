package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.converters.BookConverter;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.models.dto.BookDto;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;

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

    private final GenreRepository genreRepository;

    private final CommentService commentService;

    private final BookConverter bookConverter;


 
    @Override
    @Transactional
    public Mono<Book> insert(String title, String authorId, Set<String> genresId) {
        return save(null, title, authorId, genresId);
    }

    @Override
    @Transactional
    public Mono<Book> update(String id, String title, String authorId, Set<String> genresId) {
        return save(id, title, authorId, genresId);
    }

    @Override
    @Transactional
    public void deleteById(String bookId) {
        commentService.deleteByBookId(bookId);
        bookRepository.deleteById(bookId);

    }


    @Transactional
    public Mono<Book> save(String id, String title, String authorId, Set<String> genresId) {
        return genreRepository.findAllById(genresId).collectList().flatMap(GenreList -> {
            if (genresId.size() != GenreList.size()) {
                throw new EntityNotFoundException("One or all genres with ids %s not found".formatted(genresId));
            }
            return authorService.findById(authorId).flatMap(author -> {
                if (author == null) {
                    throw new EntityNotFoundException("Author with id %s not found".formatted(authorId));
                }
                return save2(id, title, author, GenreList);
            });

        });
    }


    private Mono<Book> save2(String id, String title, Author author, List<Genre> genres) {
        if (isEmpty(genres)) {
            throw new IllegalArgumentException("Genres ids must not be null");
        }


        var book = new Book(id, title, author, genres.stream().toList());
        return bookRepository.save(book);
    }
}
