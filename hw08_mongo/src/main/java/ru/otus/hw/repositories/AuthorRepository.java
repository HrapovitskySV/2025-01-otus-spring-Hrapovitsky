package ru.otus.hw.repositories;

import jakarta.annotation.Nonnull;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Author;

import java.util.List;
import java.util.Optional;

@Repository
public interface AuthorRepository extends MongoRepository<Author, String> {

    @Nonnull
    Optional<Author> findById(@Nonnull String id);

    Optional<Author> findByFullName(String fullName);

    @Nonnull
    List<Author> findAll();

    @Nonnull
    Author save(@Nonnull Author author);

    void deleteById(@Nonnull String id);

}

