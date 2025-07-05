package ru.otus.hw.repositoriesMongo;

import jakarta.annotation.Nonnull;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import ru.otus.hw.modelsMongo.GenreMongo;

import java.util.List;
import java.util.Optional;

@Repository
public interface GenreMongoRepository extends MongoRepository<GenreMongo, String> {

    @Nonnull
    Optional<GenreMongo> findById(@Nonnull String id);

    Optional<GenreMongo> findByName(String name);

    @Nonnull
    List<GenreMongo> findAll();

    @Nonnull
    GenreMongo save(@Nonnull GenreMongo genre);

    void deleteById(@Nonnull String id);

}