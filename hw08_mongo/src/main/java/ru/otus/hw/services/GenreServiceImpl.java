package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;

    private final MongoTemplate mongoTemplate;

    @Override
    public Optional<Genre> findById(String id) {
        return genreRepository.findById(id);
    }

    @Override
    public List<Genre> findAll() {
        return  genreRepository.findAll();
    }

    public void updateBookGenre(String genreId, String genreName) {
        Query query = new Query(Criteria.where("genres._id").is(genreId));
        Update update = new Update().set("genres.$.name", genreName);
        var wr = mongoTemplate.updateMulti(query,update, Book.class);
    }



    @Override
    public Genre save(String id, String name) {
        var genre = new Genre(id, name);
        updateBookGenre(id, name);
        return genreRepository.save(genre);
    }

    public void deleteBookGenre(String genreId) {
        Query query = new Query(Criteria.where("genres._id").is(genreId));
        Update update = new Update().pull("genres", new Query(Criteria.where("._id").is(genreId)));
        var wr = mongoTemplate.updateMulti(query,update,Book.class);
    }


    @Override
    @Transactional
    public void deleteById(String id) {
        deleteBookGenre(id);
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
