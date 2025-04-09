package ru.otus.hw.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Book;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends MongoRepository<Book, BigInteger> {
    Optional<Book> findById(BigInteger id);

    List<Book> findAll();

    Book save(Book book);

    void deleteById(BigInteger id);
}
