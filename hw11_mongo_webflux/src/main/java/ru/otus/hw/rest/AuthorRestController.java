package ru.otus.hw.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
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
import ru.otus.hw.models.Author;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;

@RestController
@RequiredArgsConstructor
public class AuthorRestController {

    private final AuthorRepository authorRepository;

    private final BookRepository bookRepository;

    @GetMapping("/api/authors")
    public Flux<Author> getAllAuthors() {
        return authorRepository.findAll();
    }

    @GetMapping("/api/authors/{id}")
    public Mono<ResponseEntity<Author>> getAuthor(@PathVariable("id") String id) {
            return authorRepository.findById(id)
                    .map(author -> ResponseEntity.ok(author))
                    .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PutMapping("/api/authors")
    @Transactional
    public Mono<ResponseEntity<Author>> saveAuthor(@RequestBody Author author) {
        return save(author)
                .map(savedAuthor -> new ResponseEntity<Author>(savedAuthor, HttpStatus.OK))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping("/api/authors")
    @Transactional
    public Mono<ResponseEntity<Author>> insertAuthor(@RequestBody Author author) {
        if (author.getId()=="") {
            author.setId(null);// с фронта приходит пустая строка, и прям так и записывается, а это не правильно
        }
        return save(author)
                .map(savedAuthor -> new ResponseEntity<Author>(savedAuthor, HttpStatusCode.valueOf(201)))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/api/authors/{id}")
    @Transactional
    public Mono<ResponseEntity<Void>> deleteAuthor(@PathVariable("id") String id) {
        return deleteById(id)
                .then(Mono.just(new ResponseEntity<Void>(HttpStatus.OK)))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }


    public Mono<Author> save(Author author) {
        var resMono1 = bookRepository.updateBookAuthors(author.getId(), author.getFullName());
        var savedAuthorMono = authorRepository.save(author);
        return Mono.zip(resMono1, savedAuthorMono,
                (res1, savedAuthor) -> {
                                                            return savedAuthor;
                                                        }
        );
    }



    public Mono<Boolean> deleteById(String id) {
        var resMono1 = bookRepository.deleteBookAuthors(id);
        var resMono2 = authorRepository.deleteById(id);
        return Mono.zip(resMono1, resMono2).then(Mono.just(true));
    }
}
