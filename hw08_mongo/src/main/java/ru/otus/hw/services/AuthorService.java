package ru.otus.hw.services;

import ru.otus.hw.models.Author;

import java.util.List;
import java.util.Optional;


public interface AuthorService {
    Optional<Author> findById(String id);

    List<Author> findAll();

    Author save(String id, String fullName);

    void deleteById(String id);

    Author findByNameOrCreate(String fullName);
}
