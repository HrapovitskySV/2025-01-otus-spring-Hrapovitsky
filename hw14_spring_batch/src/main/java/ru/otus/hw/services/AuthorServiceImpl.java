package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.modelsMongo.AuthorMongo;
import ru.otus.hw.repositoriesMongo.AuthorMongoRepository;
import ru.otus.hw.repositoriesMongo.BookMongoRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AuthorServiceImpl implements AuthorService {

    private final AuthorMongoRepository authorRepository;

    private final BookMongoRepository bookRepository;

    @Override
    public Optional<AuthorMongo> findById(String id) {
        return authorRepository.findById(id);
    }

    @Override
    public List<AuthorMongo> findAll() {
        return authorRepository.findAll();
    }


    @Override
    @Transactional
    public AuthorMongo save(String id, String fullName) {
        var author = new AuthorMongo(id, fullName);
        bookRepository.updateBookAuthors(id, fullName);
        return authorRepository.save(author);
    }

    @Override
    @Transactional
    public void deleteById(String id) {
        bookRepository.deleteBookAuthors(id);
        authorRepository.deleteById(id);
    }


    public AuthorMongo findByNameOrCreate(String fullName) {
        var author = authorRepository.findByFullName(fullName);
        if (author.isEmpty()) {
            return  save(null, fullName);
        }

        return author.get(0);
    }
}
