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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.springframework.shell.component.view.event.KeyEvent.Key.i;

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

        int batch = 10;

        for (int i = 1; i <= batch; i++) {
            authorRepository.save(new AuthorMongo(Integer.toString(i), String.format("Автор %d",i)));
            genreRepository.save(new GenreMongo(Integer.toString(i), String.format("Жанр %d",i)));
        }

        insertBooks(batch, bookRepository, commentRepository);
    }

    public void insertBooks(int batch,
                            BookMongoRepository bookRepository,
                            CommentMongoRepository commentRepository) {

        BookMongo book;

        int batch10 = 10 * batch;
        int j;

        String idComment;
        Random rand = new Random(1);
        int randNum;
        int randNum2;

        for (int i = 1; i <= batch10; i++) {
            randNum = rand.nextInt(1,batch);
            randNum2 = rand.nextInt(1,batch);
            book = createBook(i, randNum, randNum2);
            book = bookRepository.save(book);

            for (j = 1; j < Math.round(i / 3); j++) {
                idComment = String.format("%d_%d",i,j);
                commentRepository.save(new CommentMongo(idComment, book, "Comment_" + idComment));
            }
        }
    }

    public BookMongo createBook(int key, int randNum, int randNum2) {
        List<GenreMongo> genres = new ArrayList<>();

        genres.add(new GenreMongo(Integer.toString(randNum), String.format("Жанр %d",randNum)));
        if (randNum2 != randNum) {
            genres.add(new GenreMongo(Integer.toString(randNum2), String.format("Жанр %d",randNum2)));
        }

        var book = new BookMongo(Integer.toString(key), String.format("Книга %d", key), null, genres);
        book.setAuthor(new AuthorMongo(Integer.toString(randNum), String.format("Автор %d",randNum)));


        return book;
    }


}