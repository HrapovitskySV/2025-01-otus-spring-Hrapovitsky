package ru.otus.hw.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import ru.otus.hw.models.Book;

@RequiredArgsConstructor
public class BookRepositoryCustomImpl implements BookRepositoryCustom {

    private final ReactiveMongoTemplate mongoTemplate;

    @Override
    public void updateBookAuthors(String authorId, String authorFullName) {
        Query query = new Query(Criteria.where("author._id").is(authorId));
        Update update = new Update().set("author.full_Name", authorFullName);
        var wr = mongoTemplate.findAndModify(query,update, Book.class);
    }

    @Override
    public void deleteBookAuthors(String authorId) {
        Query query = new Query(Criteria.where("author._id").is(authorId));
        Update update = new Update().set("author", null);
        var wr = mongoTemplate.findAndModify(query,update,Book.class);
    }

    @Override
    public void updateBookGenre(String genreId, String genreName) {
        Query query = new Query(Criteria.where("genres._id").is(genreId));
        Update update = new Update().set("genres.$.name", genreName);
        var wr = mongoTemplate.updateMulti(query,update, Book.class);
    }

    @Override
    public void deleteBookGenre(String genreId) {
        Query query = new Query(Criteria.where("genres._id").is(genreId));
        Update update = new Update().pull("genres", new Query(Criteria.where("._id").is(genreId)));
        var wr = mongoTemplate.updateMulti(query,update,Book.class);
    }
}
