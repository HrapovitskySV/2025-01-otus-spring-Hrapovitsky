package ru.otus.hw.services;

import ru.otus.hw.modelsMongo.GenreMongo;

import java.util.List;
import java.util.Optional;


public interface GenreService {
    Optional<GenreMongo> findById(String id);

    List<GenreMongo> findAll();

    GenreMongo save(String id, String name);

    void deleteById(String id);

    GenreMongo findByNameOrCreate(String name);
}
