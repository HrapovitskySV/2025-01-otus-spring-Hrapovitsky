package ru.otus.hw.repositories;

import org.springframework.stereotype.Repository;

@Repository
public interface BookRepositoryCustom {

    void updateBookAuthors(String authorId, String authorFullName);

    void deleteBookAuthors(String authorId);

    void updateBookGenre(String genreId, String genreName);

    void deleteBookGenre(String genreId);
}
