package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.models.Author;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;

    private final BookRepository bookRepository;

    @Override
    public Optional<Author> findById(String id) {
        return authorRepository.findById(id);
    }

    @Override
    public List<Author> findAll() {
        return authorRepository.findAll();
    }



    @Override
    @Transactional
    public Author save(String id, String fullName) {
        var author = new Author(id, fullName);
        bookRepository.updateBookAuthors(id, fullName);
        return authorRepository.save(author);
    }



    @Override
    @Transactional
    public void deleteById(String id) {
        bookRepository.deleteBookAuthors(id);
        authorRepository.deleteById(id);
    }


    public Author findByNameOrCreate(String fullName) {
        var author = authorRepository.findByFullName(fullName);
        if (author.isEmpty()) {
            return  save(null, fullName);
        }

        return author.get(0);
    }
}
