package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.repositories.AuthorRepository;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;

    private final MongoTemplate mongoTemplate;

    @Override
    public Optional<Author> findById(String id) {
        return authorRepository.findById(id);
    }

    @Override
    public List<Author> findAll() {
        return authorRepository.findAll();
    }

    public void updateBookAuthors(String authorId, String authorFullName) {
        Query query = new Query(Criteria.where("author._id").is(authorId));
        Update update = new Update().set("author.full_Name", authorFullName);
        var wr = mongoTemplate.findAndModify(query,update,Book.class);
    }

    @Override
    public Author save(String id, String fullName) {
        var author = new Author(id, fullName);
        updateBookAuthors(id, fullName);
        return authorRepository.save(author);
    }

    public void deleteBookAuthors(String authorId) {
        Query query = new Query(Criteria.where("author._id").is(authorId));
        Update update = new Update().set("author", null);
        var wr = mongoTemplate.findAndModify(query,update,Book.class);
    }

    @Override
    @Transactional
    public void deleteById(String id) {
        deleteBookAuthors(id);
        authorRepository.deleteById(id);
    }


    public Author findByNameOrCreate(String fullName) {
        var author = authorRepository.findByFullName(fullName);
        if (author.isEmpty()) {
            return  save(null, fullName);
        }

        return author.get(0);
    }
}
