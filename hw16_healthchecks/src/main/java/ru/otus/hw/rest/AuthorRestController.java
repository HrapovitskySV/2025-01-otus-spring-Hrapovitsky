package ru.otus.hw.rest;

import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.endpoint.annotation.DeleteOperation;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.annotation.WriteOperation;
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

    private final MeterRegistry registry;

    private Logger logger = LoggerFactory.getLogger(AuthorRestController.class);

    private Counter countRestCalls;


    @PostConstruct
    public void initialize() {
        countRestCalls = Counter.builder("count_rest_calls_author").register(registry);
    }

    @GetMapping("/api/authors")
    @ReadOperation
    public List<Author> getAllAuthors() {
        countRestCalls.increment();
        return authorService.findAll();
    }

    @GetMapping("/api/authors/{id}")
    @Timed
    @ReadOperation
    public Author getAuthor(@PathVariable("id") long id) {
        countRestCalls.increment();
        return authorService.findById(id).orElseThrow(AuthorNotFoundException::new);
    }

    @PutMapping("/api/authors")
    @Timed
    @WriteOperation
    public ResponseEntity<Author> saveAuthor(@RequestBody Author author) {
        countRestCalls.increment();
        logger.info("Update Author by id: " + author.getId());
        Author savedAuthor = authorService.save(author);
        return ResponseEntity.ok(savedAuthor);
    }

    @PostMapping("/api/authors")
    @Timed
    @WriteOperation
    public ResponseEntity<Author> insertAuthor(@RequestBody Author author) {
        countRestCalls.increment();
        Author savedAuthor = authorService.save(author);
        logger.info("Write new Author");
        return ResponseEntity.status(HttpStatusCode.valueOf(201)).body(savedAuthor);
    }

    @DeleteMapping("/api/authors/{id}")
    @Timed
    @DeleteOperation
    public ResponseEntity<String> deleteAuthor(@PathVariable("id") long id) {
        countRestCalls.increment();
        logger.info("Delete Author by id: " + id);
        authorService.deleteById(id);
        return ResponseEntity.ok("");
    }
}
