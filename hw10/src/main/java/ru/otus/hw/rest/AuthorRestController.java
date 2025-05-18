package ru.otus.hw.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
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
    public List<Author> getAllAuthors() {
        List<Author> authors = authorService.findAll();
        return authors;
    }

    @GetMapping("/api/authors/{id}")
    public Author getAuthor(@PathVariable("id") long id, Model model) {
        return authorService.findById(id).orElseThrow(AuthorNotFoundException::new);
    }

    @PutMapping("/api/authors")
    public ResponseEntity<Author> saveAuthor(@RequestBody Author author) {
        Author savedAuthor = authorService.save(author);
        return ResponseEntity.ok(savedAuthor);
    }

    @PostMapping("/api/authors")
    public ResponseEntity<Author> insertAuthor(@RequestBody Author author) {
        Author savedAuthor = authorService.save(author);
        return ResponseEntity.ok(savedAuthor);
    }

    @DeleteMapping("/api/authors/{id}")
    public ResponseEntity<String> deleteuthor(@PathVariable("id") long id) {
        authorService.deleteById(id);
        return ResponseEntity.ok("");
    }
}
