package ru.otus.hw.mongock.changelog;


import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
import reactor.core.publisher.Mono;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.ArrayList;
import java.util.List;

@ChangeLog
public class DatabaseChangelog {

    @ChangeSet(order = "001", id = "dropDb", author = "stvort", runAlways = true)
    public void dropDb(MongoDatabase db) {
        db.drop();
    }



    @ChangeSet(order = "002", id = "insertColobok", author = "stvort")
    public void insertColobok(BookRepository bookRepository,
                              CommentRepository commentRepository,
                              AuthorRepository authorRepository,
                              GenreRepository genreRepository) {


        //Author savedAuthor2 = authorRepository.save(author).block();
        Genre savedGenre = genreRepository.save(new Genre("1", "сказка")).block();
        Author author = authorRepository.save(new Author("1", "people")).block();
        var savedBook=bookRepository.save(new Book("1", "Колобок", author, List.of(savedGenre))).block();
        commentRepository.save(new Comment(null, savedBook, "Cool")).block();
        commentRepository.save(new Comment(null, savedBook, "comment 2")).block();
    }

    @ChangeSet(order = "003", id = "insertNeznayka", author = "stvort")
    public void insertNeznayka(BookRepository bookRepository,
                               CommentRepository commentRepository,
                               AuthorRepository authorRepository,
                               GenreRepository genreRepository) {

        Author author = authorRepository.save(new Author("2", "Николай Носов")).block();
        Genre genre1 = genreRepository.findByName("сказка").block();
        Genre genre2 = genreRepository.save(new Genre("2", "роман")).block();

        List<Genre> genreList = List.of(genre1,genre2);
        Book book= bookRepository.save(new Book("2", "Незнайка", author, genreList)).block();

        commentRepository.save(new Comment("1", book, "Cool Незнайка")).block();
        commentRepository.save(new Comment(null, book, "comment Незнайка 2")).block();
        commentRepository.save(new Comment(null, book, "comment Незнайка 3")).block();
    }
}