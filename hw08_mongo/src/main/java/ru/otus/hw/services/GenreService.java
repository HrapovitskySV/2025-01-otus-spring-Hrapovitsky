package ru.otus.hw.services;

import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.Optional;


public interface GenreService {
    Optional<Genre> findById(String id);

    List<Genre> findAll();

    Genre save(String id, String name);

    void deleteById(String id);

    Genre findByNameOrCreate(String name);
}
