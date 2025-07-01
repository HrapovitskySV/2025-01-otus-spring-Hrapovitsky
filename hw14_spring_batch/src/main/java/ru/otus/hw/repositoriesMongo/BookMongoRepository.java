package ru.otus.hw.repositoriesMongo;

import jakarta.annotation.Nonnull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import ru.otus.hw.modelsMongo.BookMongo;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookMongoRepository extends MongoRepository<BookMongo, String>, BookMongoRepositoryCustom {

    @Nonnull
    Optional<BookMongo> findById(@Nonnull String id);

    @Nonnull
    List<BookMongo> findAll();

    @Nonnull
    Page<BookMongo> findAll(@Nonnull Pageable pageable);

    @Nonnull
    BookMongo save(@Nonnull BookMongo book);

    void deleteById(@Nonnull String id);
}
