package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.models.Author;
import ru.otus.hw.repositories.AuthorRepository;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;


    @Override
    public Optional<Author> findById(String id) {
        return authorRepository.findById(id);
    }

    @Override
    public List<Author> findAll() {
        return  authorRepository.findAll();
    }

    @Override
    public Author save(String id, String fullName) {
        var author = new Author(id, fullName);
        return authorRepository.save(author);
    }

    @Override
    public void deleteById(String id) {
        authorRepository.deleteById(id);
    }


    public Author findByNameOrCreate(String fullName) {
        var author = authorRepository.findByFullName(fullName);
        if (author.isEmpty()) {
            return  save(null, fullName);
        }

        return author.get();
    }
}
