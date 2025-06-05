package ru.otus.hw.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
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
import ru.otus.hw.models.Author;
import ru.otus.hw.repositories.AuthorRepository;

@RestController
@RequiredArgsConstructor
public class AuthorRestController {

    private final AuthorRepository authorRepository;


    @GetMapping("/api/authors")
    public Flux<Author> getAllAuthors() {
        return authorRepository.findAll();
    }

    @GetMapping("/api/authors/{id}")
    public Mono<ResponseEntity<Author>> getAuthor(@PathVariable("id") String id) {
            return authorRepository.findById(id)
                    .map(author -> ResponseEntity.ok(author))
                    .defaultIfEmpty(ResponseEntity.notFound().build());
                    //.defaultIfEmpty(Mono.error(new AuthorNotFoundException));
        //return authorService.findById(id).orElseThrow(AuthorNotFoundException::new);
    }

    @PutMapping("/api/authors")
    public Mono<ResponseEntity<Author>> saveAuthor(@RequestBody Author author) {
        //Mono<Author> savedAuthor = authorRepository.save(author);
        return authorRepository.save(author)
                .map(savedAuthor -> new ResponseEntity<Author>(savedAuthor, HttpStatus.OK))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping("/api/authors")
    public Mono<ResponseEntity<Author>> insertAuthor(@RequestBody Author author) {
        return authorRepository.save(author)
                .map(savedAuthor -> new ResponseEntity<Author>(savedAuthor, HttpStatusCode.valueOf(201)))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/api/authors/{id}")
    public Mono<ResponseEntity<Void>> deleteAuthor(@PathVariable("id") String id) {
        return authorRepository.deleteById(id)
                .then(Mono.just(new ResponseEntity<Void>(HttpStatus.OK)))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
