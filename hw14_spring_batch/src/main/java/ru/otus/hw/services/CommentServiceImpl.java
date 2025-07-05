package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.converters.CommentConverter;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.modelsMongo.BookMongo;
import ru.otus.hw.modelsMongo.CommentMongo;
import ru.otus.hw.modelsMongo.dto.CommentMongoDto;
import ru.otus.hw.repositoriesMongo.BookMongoRepository;
import ru.otus.hw.repositoriesMongo.CommentMongoRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {

    private final CommentMongoRepository commentRepository;

    private final BookMongoRepository bookRepository;

    private final CommentConverter commentConverter;


    @Override
    public Optional<CommentMongo> findById(String id) {
        return commentRepository.findById(id);
    }

    @Override
    public List<CommentMongoDto> findAll() {
        var comments = commentRepository.findAll();
        return comments.stream().map(commentConverter::toDto).toList();
    }

    @Override
    public List<CommentMongoDto> findByBookId(String bookId) {
        var book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book with id %s not found".formatted(bookId)));

        var comments = commentRepository.findByBook(book);
        return comments.stream().map(commentConverter::toDto).toList();
    }

    @Override
    public CommentMongo insert(String comment, String bookId) {
        return save(null, comment, bookId);
    }

    @Override
    public CommentMongo update(String id, String comment, String bookId) {
        return save(id, comment, bookId);
    }

    @Override
    public void deleteById(String id) {
        commentRepository.deleteById(id);
    }

    @Override
    public void deleteByBookId(String bookId) {
        //var book = bookRepository.findById(bookId)
        //        .orElseThrow(() -> new EntityNotFoundException("Book with id %d not found".formatted(bookId)));

        //var book = new Book(bookId,null,null,null);
        //commentRepository.deleteByBook(book);
        commentRepository.deleteByBookId(bookId);

    }

    @Override
    public void deleteByBook(BookMongo book) {
        commentRepository.deleteByBook(book);
    }


    private CommentMongo save(String id, String commentText, String bookId) {

        var book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book with id %s not found".formatted(bookId)));

        var comment = new CommentMongo(id, book, commentText);
        return commentRepository.save(comment);
    }
}
