package ru.otus.hw.repositories;

import jakarta.annotation.Nonnull;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.Optional;

@Repository
public interface GenreRepository extends MongoRepository<Genre, String> {

    @Nonnull
    Optional<Genre> findById(@Nonnull String id);

    Optional<Genre> findByName(String name);

    @Nonnull
    List<Genre> findAll();

    @Nonnull
    Genre save(@Nonnull Genre genre);

    void deleteById(@Nonnull String id);

}