package ru.otus.hw.repositories;

import jakarta.annotation.Nonnull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Book;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends MongoRepository<Book, String>, BookRepositoryCustom {

    @Nonnull
    Optional<Book> findById(@Nonnull String id);

    @Nonnull
    List<Book> findAll();

    @Nonnull
    Page<Book> findAll(@Nonnull Pageable pageable);

    @Nonnull
    Book save(@Nonnull Book book);

    void deleteById(@Nonnull String id);
}
