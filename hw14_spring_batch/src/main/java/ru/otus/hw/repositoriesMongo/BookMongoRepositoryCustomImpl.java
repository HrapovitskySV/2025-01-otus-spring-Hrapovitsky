package ru.otus.hw.repositoriesMongo;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import ru.otus.hw.exceptions.EntityUsageException;
import ru.otus.hw.modelsMongo.BookMongo;

@RequiredArgsConstructor
public class BookMongoRepositoryCustomImpl implements BookMongoRepositoryCustom {

    private final MongoTemplate mongoTemplate;

    @Override
    public void updateBookAuthors(String authorId, String authorFullName) {
        Query query = new Query(Criteria.where("author._id").is(authorId));
        Update update = new Update().set("author.full_Name", authorFullName);
        var wr = mongoTemplate.updateMulti(query,update, BookMongo.class);
    }

    @Override
    public void deleteBookAuthors(String authorId) {
        Query query = new Query(Criteria.where("author._id").is(authorId));
        //Update update = new Update().set("author", null);
        //var wr = mongoTemplate.updateMulti(query,update,Book.class);
        long c = mongoTemplate.count(query,BookMongo.class);

        if (c > 0) {
            throw new EntityUsageException("One or all book found with authorId %s ".formatted(authorId));
        }
    }

    @Override
    public void updateBookGenre(String genreId, String genreName) {
        Query query = new Query(Criteria.where("genres._id").is(genreId));
        Update update = new Update().set("genres.$.name", genreName);
        var wr = mongoTemplate.updateMulti(query,update, BookMongo.class);
    }

    @Override
    public void deleteBookGenre(String genreId) {
        Query query = new Query(Criteria.where("genres._id").is(genreId));
        Update update = new Update().pull("genres", new Query(Criteria.where("._id").is(genreId)));
        var wr = mongoTemplate.updateMulti(query,update,BookMongo.class);
    }
}
