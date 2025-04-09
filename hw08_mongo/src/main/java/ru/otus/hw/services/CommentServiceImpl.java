package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.converters.CommentConverter;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.dto.CommentDto;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    private final BookRepository bookRepository;

    private final CommentConverter commentConverter;


    @Override
    public Optional<Comment> findById(BigInteger id) {
        return commentRepository.findById(id);
    }

    @Override
    public List<CommentDto> findAll() {
        var comments = commentRepository.findAll();
        return comments.stream().map(commentConverter::toDto).toList();
    }

    @Override
    public List<CommentDto> findByBookId(BigInteger bookId) {
        var book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book with id %d not found".formatted(bookId)));

        var comments = commentRepository.findByBook(book);
        return comments.stream().map(commentConverter::toDto).toList();
    }

    @Override
    public Comment insert(String comment, BigInteger bookId) {
        return save(null, comment, bookId);
    }

    @Override
    public Comment update(BigInteger id, String comment, BigInteger bookId) {
        return save(id, comment, bookId);
    }

    @Override
    public void deleteById(BigInteger id) {
        commentRepository.deleteById(id);
    }

    @Override
    public void deleteByBookId(BigInteger bookId) {
        //var book = bookRepository.findById(bookId)
        //        .orElseThrow(() -> new EntityNotFoundException("Book with id %d not found".formatted(bookId)));

        var book = new Book(bookId,null,null,null);
        commentRepository.deleteByBook(book);
    }

    @Override
    public void deleteByBook(Book book) {
        commentRepository.deleteByBook(book);
    }


    private Comment save(BigInteger id, String commentText, BigInteger bookId) {

        var book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book with id %d not found".formatted(bookId)));

        var comment = new Comment(id, book, commentText);
        return commentRepository.save(comment);
    }
}
