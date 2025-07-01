package ru.otus.hw.repositoriesMongo;

import jakarta.annotation.Nonnull;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import ru.otus.hw.modelsMongo.BookMongo;
import ru.otus.hw.modelsMongo.CommentMongo;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentMongoRepository extends MongoRepository<CommentMongo, String> {

    @Nonnull
    Optional<CommentMongo> findById(@Nonnull String id);

    @Nonnull
    List<CommentMongo>  findAll();

    List<CommentMongo> findByBook(BookMongo book);

    @Nonnull
    CommentMongo save(@Nonnull CommentMongo comment);

    void deleteById(@Nonnull String id);

    void deleteByBook(BookMongo book);

    void deleteByBookId(@Nonnull String bookId);
}
