package ru.otus.hw.repositoriesJpa;

import jakarta.annotation.Nonnull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.otus.hw.modelsJpa.Genre;

import java.util.List;
import java.util.Set;

public interface GenreRepository  extends JpaRepository<Genre, Long> {

    @Nonnull
    List<Genre> findAll();

    @Query("select g from Genre g where g.id in (:ids)")
    List<Genre> findAllByIds(@Param("ids") Set<Long> ids);


    @Nonnull
    List<Genre> findAllById(@Nonnull Iterable<Long> id);
}
