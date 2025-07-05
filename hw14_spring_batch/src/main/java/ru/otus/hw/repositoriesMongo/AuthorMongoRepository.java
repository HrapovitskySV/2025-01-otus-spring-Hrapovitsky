package ru.otus.hw.repositoriesMongo;

import jakarta.annotation.Nonnull;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import ru.otus.hw.modelsMongo.AuthorMongo;

import java.util.List;
import java.util.Optional;

@Repository
public interface AuthorMongoRepository extends MongoRepository<AuthorMongo, String> {

    @Nonnull
    Optional<AuthorMongo> findById(@Nonnull String id);

    List<AuthorMongo> findByFullName(String fullName);

    @Nonnull
    List<AuthorMongo> findAll();

    @Nonnull
    AuthorMongo save(@Nonnull AuthorMongo author);

    void deleteById(@Nonnull String id);

}

