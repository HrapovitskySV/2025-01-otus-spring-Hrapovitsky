package ru.otus.hw.repositories;

import com.mongodb.client.result.UpdateResult;
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
    public Mono<UpdateResult> updateBookAuthors(String authorId, String authorFullName) {
        Query query = new Query(Criteria.where("author._id").is(authorId));
        Update update = new Update().set("author.full_Name", authorFullName);
        return mongoTemplate.updateMulti(query,update, Book.class);
    }

    @Override
    public Mono<UpdateResult> deleteBookAuthors(String authorId) {
        Query query = new Query(Criteria.where("author._id").is(authorId));
        Update update = new Update().set("author", null);
        return mongoTemplate.updateMulti(query,update,Book.class);
    }

    @Override
    public Mono<UpdateResult> updateBookGenre(String genreId, String genreName) {
        Query query = new Query(Criteria.where("genres._id").is(genreId));
        Update update = new Update().set("genres.$.name", genreName);
        return mongoTemplate.updateMulti(query,update, Book.class);
    }

    @Override
    public Mono<UpdateResult> deleteBookGenre(String genreId) {
        Query query = new Query(Criteria.where("genres._id").is(genreId));
        Update update = new Update().pull("genres", new Query(Criteria.where("._id").is(genreId)));
        return mongoTemplate.updateMulti(query,update,Book.class);
    }
}
