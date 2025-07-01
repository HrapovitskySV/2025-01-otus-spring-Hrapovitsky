package ru.otus.hw.repositoriesJpa;

import jakarta.annotation.Nonnull;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.hw.modelsJpa.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorRepository  extends JpaRepository<Author, Long> {

    @Nonnull
    List<Author> findAll();

    Optional<Author> findById(long id);
}
