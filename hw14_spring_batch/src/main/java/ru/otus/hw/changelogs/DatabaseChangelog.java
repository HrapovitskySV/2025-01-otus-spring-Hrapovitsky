package ru.otus.hw.changelogs;


import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
import ru.otus.hw.modelsMongo.AuthorMongo;
import ru.otus.hw.modelsMongo.BookMongo;
import ru.otus.hw.modelsMongo.CommentMongo;
import ru.otus.hw.modelsMongo.GenreMongo;
import ru.otus.hw.repositoriesMongo.AuthorMongoRepository;
import ru.otus.hw.repositoriesMongo.BookMongoRepository;
import ru.otus.hw.repositoriesMongo.CommentMongoRepository;
import ru.otus.hw.repositoriesMongo.GenreMongoRepository;

import java.util.List;
import java.util.Random;

@ChangeLog
public class DatabaseChangelog {

    @ChangeSet(order = "001", id = "dropDb", author = "SHr", runAlways = true)
    public void dropDb(MongoDatabase db) {
        db.drop();
    }



    @ChangeSet(order = "002", id = "insertData", author = "SHr")
    public void insertData(BookMongoRepository bookRepository,
                           CommentMongoRepository commentRepository,
                           AuthorMongoRepository authorRepository,
                           GenreMongoRepository genreRepository) {

        AuthorMongo author;
        GenreMongo genre;
        GenreMongo genre2;
        CommentMongo comment;
        int i;
        int j;
        int batch = 100;
        int batch10 = 10*batch;

        String idComment;
        int randomNumber;
        Random rand = new Random(1);

        for(i = 1; i <= batch; i++) {
            authorRepository.save(new AuthorMongo(Integer.toString(i), String.format("Автор %d",i)));
            genreRepository.save(new GenreMongo(Integer.toString(i), String.format("Жанр %d",i)));
        }
/*
        for(i = 1; i <= batch10; i++) {
            randomNumber = rand.nextInt(1,batch10);
            author = new AuthorMongo(Integer.toString(randomNumber), String.format("Автор %d",randomNumber));
            genre = new GenreMongo(Integer.toString(randomNumber), String.format("Жанр %d",randomNumber));
            randomNumber = rand.nextInt(1,batch10);
            genre2 = new GenreMongo(Integer.toString(randomNumber), String.format("Жанр %d",randomNumber));
            BookMongo book = bookRepository.save(new BookMongo(Integer.toString(i), String.format("Книга %d",i), author, List.of(genre, genre2)));
            for(j = 1; j < Math.round(i/3); j++) {
                idComment = String.format("%d_%d",i,j);
                comment = new CommentMongo(idComment, book, "Comment_" + idComment);
                commentRepository.save(comment);
            }
        }

 */

    }

}