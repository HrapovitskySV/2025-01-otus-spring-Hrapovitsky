package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import ru.otus.hw.converters.BookConverter;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.Set;

import static org.springframework.util.CollectionUtils.isEmpty;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;


    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final CommentRepository commentRepository;

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
        commentRepository.deleteByBookId(bookId);
        bookRepository.deleteById(bookId);

    }

    private Mono<Author> authorNotFound(String authorId) {
        throw new EntityNotFoundException("Author with id %s not found".formatted(authorId));
    }

    @Transactional
    public Mono<Book> save(String id, String title, String authorId, Set<String> genresId) {
        if (isEmpty(genresId)) {
            throw new IllegalArgumentException("Genres ids must not be null");
        }

        return genreRepository.findAllById(genresId).collectList().flatMap(genreList -> {
            if (genresId.size() != genreList.size()) {
                throw new EntityNotFoundException("One or all genres with ids %s not found".formatted(genresId));
            }
            return authorRepository.findById(authorId)
                            .switchIfEmpty(Mono.defer(() -> authorNotFound(authorId)))
                            .flatMap(author -> {
                                if (author == null) {
                                    throw new EntityNotFoundException("Author with id %s not found".formatted(authorId));
                                }
                                var book = new Book(id, title, author, genreList);
                                return bookRepository.save(book);
            });

        });
    }

}
