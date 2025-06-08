package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import ru.otus.hw.models.Author;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;

@RequiredArgsConstructor
@Service
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;

    private final BookRepository bookRepository;


    @Override
    public Mono<Author> save(Author author) {
        bookRepository.updateBookAuthors(author.getId(), author.getFullName());
        return authorRepository.save(author);
    }


    @Override
    @Transactional
    public Mono<Void> deleteById(String id) {
        bookRepository.deleteBookAuthors(id);
        return authorRepository.deleteById(id);
    }

}
