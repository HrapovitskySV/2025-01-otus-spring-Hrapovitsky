package ru.otus.hw.rest;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
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
import ru.otus.hw.exceptions.AuthorNotFoundException;
import ru.otus.hw.models.Author;
import ru.otus.hw.services.AuthorService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AuthorRestController {
    private final AuthorService authorService;


    @GetMapping("/api/authors")
    @HystrixCommand(fallbackMethod = "getAllAuthorsDefault")
    public List<Author> getAllAuthors() {
        List<Author> authors = authorService.findAll();
        return authors;
    }

    public List<Author> getAllAuthorsDefault() {
        return List.of(new Author(1,"Author <>"));
    }

    @HystrixCommand(fallbackMethod = "getAuthorDefault")
    @GetMapping("/api/authors/{id}")
    public Author getAuthor(@PathVariable("id") long id) {
        return authorService.findById(id).orElseThrow(AuthorNotFoundException::new);
    }

    public Author getAuthorDefault(@PathVariable("id") long id) {
        return new Author(id,"Author <>");
    }

    @HystrixCommand
    @PutMapping("/api/authors")
    public ResponseEntity<Author> saveAuthor(@RequestBody Author author) {
        Author savedAuthor = authorService.save(author);
        return ResponseEntity.ok(savedAuthor);
    }

    @HystrixCommand
    @PostMapping("/api/authors")
    public ResponseEntity<Author> insertAuthor(@RequestBody Author author) {
        Author savedAuthor = authorService.save(author);
        return ResponseEntity.status(HttpStatusCode.valueOf(201)).body(savedAuthor);
    }

    @HystrixCommand
    @DeleteMapping("/api/authors/{id}")
    public ResponseEntity<String> deletAuthor(@PathVariable("id") long id) {
        authorService.deleteById(id);
        return ResponseEntity.ok("");
    }
}
