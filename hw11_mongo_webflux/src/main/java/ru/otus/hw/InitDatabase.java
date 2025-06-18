package ru.otus.hw;


import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.stereotype.Component;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;

@Component
@RequiredArgsConstructor
public class InitDatabase implements ApplicationRunner {

    private final BookRepository bookRepository;

    private final CommentRepository commentRepository;

    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final ReactiveMongoTemplate mongoTemplate;

    public void dropDb() {
        mongoTemplate.getMongoDatabase().block().drop();
    }


    public void insertColobok() {
        Genre savedGenre = genreRepository.save(new Genre("1", "сказка")).block();
        Author author = authorRepository.save(new Author("1", "people")).block();
        var savedBook = bookRepository.save(new Book("1", "Колобок", author, List.of(savedGenre))).block();
        commentRepository.save(new Comment(null, savedBook, "Cool")).block();
        commentRepository.save(new Comment(null, savedBook, "comment 2")).block();
    }


    public void insertNeznayka() {

        Author author = authorRepository.save(new Author("2", "Николай Носов")).block();
        Genre genre1 = genreRepository.findByName("сказка").block();
        Genre genre2 = genreRepository.save(new Genre("2", "роман")).block();

        List<Genre> genreList = List.of(genre1,genre2);
        Book book = bookRepository.save(new Book("2", "Незнайка", author, genreList)).block();

        commentRepository.save(new Comment("1", book, "Cool Незнайка")).block();
        commentRepository.save(new Comment(null, book, "comment Незнайка 2")).block();
        commentRepository.save(new Comment(null, book, "comment Незнайка 3")).block();
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        dropDb();
        insertColobok();
        insertNeznayka();

    }
}