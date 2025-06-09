package ru.otus.hw.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import ru.otus.hw.converters.BookConverter;
import ru.otus.hw.exceptions.AuthorNotFoundException;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.dto.BookDto;
import ru.otus.hw.models.dto.BookDtoInputWeb;
import ru.otus.hw.models.dto.BookDtoWeb;
import ru.otus.hw.services.BookService;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class BookRestController {
    private final BookService bookService;

    private final BookConverter bookConverter;

    @GetMapping("/authenticated/api/books")
    public List<BookDtoWeb> listAllBooks() {
        List<BookDto> books = bookService.findAll();
        return books.stream().map(bookDto -> bookConverter.toDtoWeb(bookDto)).toList();
    }

    @GetMapping("/authenticated/api/books/{id}")
    public Book getBook(@PathVariable("id") long id) {
        return bookService.findById(id).orElseThrow(AuthorNotFoundException::new);
    }

    @PutMapping("/authenticated/api/books")
    public ResponseEntity<String> saveBook(@RequestBody BookDtoInputWeb bookDto) {
        Book savedBook = bookService.update(
                bookDto.getId(),
                bookDto.getTitle(),
                bookDto.getAuthor(),
                bookDto.getGenres());
        return ResponseEntity.ok("savedBook");
    }

    @PostMapping("/authenticated/api/books")
    public ResponseEntity<Book> insertBook(@RequestBody BookDtoInputWeb bookDto) {
        Book savedBook = bookService.insert(bookDto.getTitle(), bookDto.getAuthor(), bookDto.getGenres());
        return ResponseEntity.ok(savedBook);
    }

    @DeleteMapping("/authenticated/api/books/{id}")
    public ResponseEntity<String> deletePage(@PathVariable("id") long id) {
        bookService.deleteById(id);
        return ResponseEntity.ok("deleted");
    }
}
