package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.modelsMongo.GenreMongo;
import ru.otus.hw.repositoriesMongo.BookMongoRepository;
import ru.otus.hw.repositoriesMongo.GenreMongoRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class GenreServiceImpl implements GenreService {

    private final GenreMongoRepository genreRepository;

    private final BookMongoRepository bookRepository;

    @Override
    public Optional<GenreMongo> findById(String id) {
        return genreRepository.findById(id);
    }

    @Override
    public List<GenreMongo> findAll() {
        return  genreRepository.findAll();
    }

    @Override
    @Transactional
    public GenreMongo save(String id, String name) {
        var genre = new GenreMongo(id, name);
        bookRepository.updateBookGenre(id, name);
        return genreRepository.save(genre);
    }

    @Override
    @Transactional
    public void deleteById(String id) {
        bookRepository.deleteBookGenre(id);
        genreRepository.deleteById(id);
    }


    public GenreMongo findByNameOrCreate(String name) {
        var genre = genreRepository.findByName(name);
        if (genre.isEmpty()) {
            return  save(null, name);
        }

        return genre.get();
    }
}
