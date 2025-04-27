package ru.otus.hw.mongock.changelog;


import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;
import ru.otus.hw.repositories.GenreRepository;

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

        Author author = new Author(null, "people");
        author = authorRepository.save(author);

        Genre genre = new Genre(null, "сказка");
        genreRepository.save(genre);

        Book book = bookRepository.save(new Book(null, "Колобок", author, List.of(genre)));

        commentRepository.save(new Comment(null, book, "Cool"));
        commentRepository.save(new Comment(null, book, "comment 2"));
    }

    @ChangeSet(order = "003", id = "insertNeznayka", author = "stvort")
    public void insertNeznayka(BookRepository bookRepository,
                               CommentRepository commentRepository,
                               AuthorRepository authorRepository,
                               GenreRepository genreRepository) {

        Author author = new Author(null, "Николай Носов");
        author = authorRepository.save(author);
        var opGenre1 = genreRepository.findByName("сказка");
        Genre genre1;
        if (opGenre1.isEmpty()) {
            genre1 = new Genre(null, "сказка");
            author = authorRepository.save(author);
        } else {
            genre1 = opGenre1.get();
        }

        Genre genre2 = new Genre(null, "роман");
        genreRepository.save(genre1);
        genreRepository.save(genre2);
        Book book = bookRepository.save(new Book("1", "Незнайка", author, List.of(genre1, genre2)));

        commentRepository.save(new Comment("1", book, "Cool Незнайка"));
        commentRepository.save(new Comment(null, book, "comment Незнайка 2"));
        commentRepository.save(new Comment(null, book, "comment Незнайка 3"));
    }
}