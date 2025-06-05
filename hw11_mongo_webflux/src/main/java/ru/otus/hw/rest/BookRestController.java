package ru.otus.hw.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import ru.otus.hw.exceptions.AuthorNotFoundException;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.dto.BookDto;
import ru.otus.hw.models.dto.BookDtoInputWeb;
import ru.otus.hw.models.dto.BookDtoWeb;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.services.BookService;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.defaultIfEmpty;

@RestController
@RequiredArgsConstructor
public class BookRestController {
    private final BookService bookService;

    private final BookRepository bookRepository;

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
        return bookService.update(
                        bookDto.getId(),
                        bookDto.getTitle(),
                        bookDto.getAuthor(),
                        bookDto.getGenres())
                .map(savedBook -> ResponseEntity.ok(savedBook))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping("api/books")
    public Mono<ResponseEntity<Book>> insertBook(@RequestBody BookDtoInputWeb bookDto) {
        return bookService.insert(
                        bookDto.getTitle(),
                        bookDto.getAuthor(),
                        bookDto.getGenres())
                .map(savedBook -> ResponseEntity.ok(savedBook))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/api/books/{id}")
    public Mono<ResponseEntity<Void>> deletePage(@PathVariable("id") String id) {
        return bookRepository.deleteById(id)
                .then(Mono.just(new ResponseEntity<Void>(HttpStatus.OK)))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
