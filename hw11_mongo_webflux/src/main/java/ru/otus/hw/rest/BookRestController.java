package ru.otus.hw.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.converters.BookConverter;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.dto.BookDtoInputWeb;
import ru.otus.hw.models.dto.BookDtoWeb;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.Set;

import static org.springframework.util.CollectionUtils.isEmpty;

@RestController
@RequiredArgsConstructor
public class BookRestController {
    private final BookRepository bookRepository;

    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final CommentRepository commentRepository;

    private final BookConverter bookConverter;

    @GetMapping("api/books")
    public Flux<BookDtoWeb> listAllBooks() {
        return bookRepository.findAll().map(book -> bookConverter.toDtoWeb(book));
    }

    @GetMapping("api/books/{id}")
    public Mono<ResponseEntity<Book>> getBook(@PathVariable("id") String id) {
        return bookRepository.findById(id)
                .map(book -> ResponseEntity.ok(book))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PutMapping("/api/books")
    public Mono<ResponseEntity<Book>> saveBook(@RequestBody BookDtoInputWeb bookDto) {
        return saveBook(
                        bookDto.getId(),
                        bookDto.getTitle(),
                        bookDto.getAuthor(),
                        bookDto.getGenres())
                .map(savedBook -> ResponseEntity.ok(savedBook))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping("api/books")
    public Mono<ResponseEntity<Book>> insertBook(@RequestBody BookDtoInputWeb bookDto) {
        return saveBook(null,
                        bookDto.getTitle(),
                        bookDto.getAuthor(),
                        bookDto.getGenres())
                .map(savedBook -> ResponseEntity.ok(savedBook))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/api/books/{id}")
    public Mono<ResponseEntity<Void>> deleteBook(@PathVariable("id") String id) {
        return bookRepository.deleteById(id)
                .then(commentRepository.deleteByBookId(id))
                .then(Mono.just(new ResponseEntity<Void>(HttpStatus.OK)));
    }

    @Transactional
    public Mono<Book> saveBook(String id, String title, String authorId, Set<String> genresId) {
        if (isEmpty(genresId)) {
            throw new IllegalArgumentException("Genres ids must not be null");
        }

        var genresListMono = genreRepository.findAllById(genresId).collectList();

        var authorMono = authorRepository.findById(authorId)
                .switchIfEmpty(
                        Mono.error(new EntityNotFoundException("Author with id %s not found".formatted(authorId)))
                );

        return Mono.zip(genresListMono, authorMono, (genreList, author) -> {
                    if (genresId.size() != genreList.size()) {
                        throw new EntityNotFoundException("One or all genres with ids %s not found".formatted(genresId));
                    }

                    if (author == null) {
                        throw new EntityNotFoundException("Author with id %s not found".formatted(authorId));
                    }
                    return new Book(id, title, author, genreList);
                }
        ).flatMap(savedBook -> bookRepository.save(savedBook));
    }
}
