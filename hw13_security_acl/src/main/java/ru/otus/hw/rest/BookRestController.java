package ru.otus.hw.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
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
import ru.otus.hw.models.dto.BookDto;
import ru.otus.hw.models.dto.BookDtoInputWeb;
import ru.otus.hw.models.dto.BookDtoWeb;
import ru.otus.hw.services.BookServiceWrapperService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BookRestController {

    private final BookConverter bookConverter;

    private final BookServiceWrapperService bookServiceWrapperService;



    @GetMapping("/api/books")
    public List<BookDtoWeb> listAllBooks() {
        List<BookDto> books = bookServiceWrapperService.findAll();
        return books.stream().map(bookDto -> bookConverter.toDtoWeb(bookDto)).toList();
    }

    @GetMapping("/api/books/{id}")
    public BookDto getBook(@PathVariable("id") long id) {
        return bookServiceWrapperService.findById(id).orElseThrow(AuthorNotFoundException::new);
    }

    @PutMapping("/api/books")
    public ResponseEntity<String> saveBook(@RequestBody BookDtoInputWeb bookDto) {
        BookDto savedBook = bookServiceWrapperService.update(
                bookDto.getId(),
                bookDto.getTitle(),
                bookDto.getAuthor(),
                bookDto.getGenres());
        return ResponseEntity.ok("savedBook");
    }

    @PostMapping("/api/books")
    public ResponseEntity<BookDto> insertBook(@RequestBody BookDtoInputWeb bookDto) {
        BookDto savedBook = bookServiceWrapperService.insert(bookDto.getTitle(),
                                                                bookDto.getAuthor(),
                                                                bookDto.getGenres());
        return ResponseEntity.status(HttpStatusCode.valueOf(201)).body(savedBook);
    }

    @DeleteMapping("/api/books/{id}")
    public ResponseEntity<String> deletePage(@PathVariable("id") long id) {
        bookServiceWrapperService.deleteById(id);
        return ResponseEntity.ok("deleted");
    }
}
