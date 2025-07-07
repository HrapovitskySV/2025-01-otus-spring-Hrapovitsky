package ru.otus.hw.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import reactor.core.publisher.Mono;
import ru.otus.hw.models.Book;


@RequiredArgsConstructor
public class BookRepositoryCustomImpl implements BookRepositoryCustom {

    private final ReactiveMongoTemplate mongoTemplate;

    @Override
    public Mono<Boolean> updateBookAuthors(String authorId, String authorFullName) {
        Query query = new Query(Criteria.where("author._id").is(authorId));
        Update update = new Update().set("author.full_Name", authorFullName);
        return mongoTemplate.updateMulti(query,update, Book.class).map(rez -> {
            return true;
        });

    }

    @Override
    public Mono<Boolean> deleteBookAuthors(String authorId) {
        Query query = new Query(Criteria.where("author._id").is(authorId));
        Update update = new Update().set("author", null);
        return mongoTemplate.updateMulti(query,update,Book.class).map(rez -> {
            return true;
        });
    }

}
