package ru.otus.hw.services;

import ru.otus.hw.modelsMongo.AuthorMongo;

import java.util.List;
import java.util.Optional;


public interface AuthorService {
    Optional<AuthorMongo> findById(String id);

    List<AuthorMongo> findAll();

    AuthorMongo save(String id, String fullName);

    void deleteById(String id);

    AuthorMongo findByNameOrCreate(String fullName);
}
