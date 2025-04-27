package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;


    @Override
    public Optional<Genre> findById(String id) {
        return genreRepository.findById(id);
    }

    @Override
    public List<Genre> findAll() {
        return  genreRepository.findAll();
    }

    @Override
    public Genre save(String id, String name) {
        var genre = new Genre(id, name);
        return genreRepository.save(genre);
    }

    @Override
    public void deleteById(String id) {
        genreRepository.deleteById(id);
    }


    public Genre findByNameOrCreate(String name) {
        var genre = genreRepository.findByName(name);
        if (genre.isEmpty()) {
            return  save(null, name);
        }

        return genre.get();
    }
}
