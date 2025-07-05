package ru.otus.hw.repositoriesMongo;

import org.springframework.stereotype.Repository;

@Repository
public interface BookMongoRepositoryCustom {

    void updateBookAuthors(String authorId, String authorFullName);

    void deleteBookAuthors(String authorId);

    void updateBookGenre(String genreId, String genreName);

    void deleteBookGenre(String genreId);
}
