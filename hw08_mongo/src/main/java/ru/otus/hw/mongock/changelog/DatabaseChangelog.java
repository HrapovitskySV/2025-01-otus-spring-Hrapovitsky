package ru.otus.hw.mongock.changelog;


import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;

import java.math.BigInteger;
import java.util.List;

@ChangeLog
public class DatabaseChangelog {

    @ChangeSet(order = "001", id = "dropDb", author = "stvort", runAlways = true)
    public void dropDb(MongoDatabase db) {
        db.drop();
    }



    @ChangeSet(order = "002", id = "insertColobok", author = "stvort")
    public void insertColobok(BookRepository bookRepository, CommentRepository commentRepository) {
        Author author = new Author(1, "people");
        Genre genre = new Genre(1, "сказка");
        Book book = bookRepository.save(new Book(null, "Колобок", author, List.of(genre)));

        commentRepository.save(new Comment(null, book, "Cool"));

        commentRepository.save(new Comment(null, book, "comment 2"));

    }

    @ChangeSet(order = "003", id = "insertNeznayka", author = "stvort")
    public void insertNeznayka(BookRepository bookRepository, CommentRepository commentRepository) {
        Author author = new Author(1, "Николай Носов");
        Genre genre1 = new Genre(1, "сказка");
        Genre genre2 = new Genre(1, "роман");
        Book book = bookRepository.save(new Book(BigInteger.ONE, "Незнайка", author, List.of(genre1, genre2)));

        commentRepository.save(new Comment(BigInteger.ONE, book, "Cool Незнайка"));

        commentRepository.save(new Comment(null, book, "comment Незнайка 2"));
        commentRepository.save(new Comment(null, book, "comment Незнайка 3"));

    }
}