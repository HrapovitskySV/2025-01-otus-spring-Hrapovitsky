package ru.otus.hw.services;

import ru.otus.hw.modelsJpa.Genre;

import java.util.List;

public interface GenreService {
    List<Genre> findAll();
}
