package ru.otus.hw.repositories;

import com.mongodb.client.result.UpdateResult;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface BookRepositoryCustom {

    Mono<UpdateResult> updateBookAuthors(String authorId, String authorFullName);

    Mono<UpdateResult> deleteBookAuthors(String authorId);

    Mono<UpdateResult> updateBookGenre(String genreId, String genreName);

    Mono<UpdateResult> deleteBookGenre(String genreId);
}
