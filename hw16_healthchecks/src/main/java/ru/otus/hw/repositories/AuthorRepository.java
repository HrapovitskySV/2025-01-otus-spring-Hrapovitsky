package ru.otus.hw.repositories;

import jakarta.annotation.Nonnull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.otus.hw.models.Author;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource(collectionResourceRel = "authors", path = "authors")
public interface AuthorRepository  extends JpaRepository<Author, Long> {

    @Nonnull
    List<Author> findAll();

    Optional<Author> findById(long id);

    @Override
    long count();
}
