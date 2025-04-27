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

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    private final BookRepository bookRepository;

    private final CommentConverter commentConverter;


    @Override
    public Optional<Comment> findById(String id) {
        return commentRepository.findById(id);
    }

    @Override
    public List<CommentDto> findAll() {
        var comments = commentRepository.findAll();
        return comments.stream().map(commentConverter::toDto).toList();
    }

    @Override
    public List<CommentDto> findByBookId(String bookId) {
        var book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book with id %s not found".formatted(bookId)));

        var comments = commentRepository.findByBook(book);
        return comments.stream().map(commentConverter::toDto).toList();
    }

    @Override
    public Comment insert(String comment, String bookId) {
        return save(null, comment, bookId);
    }

    @Override
    public Comment update(String id, String comment, String bookId) {
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

        var book = new Book(bookId,null,null,null);
        commentRepository.deleteByBook(book);
    }

    @Override
    public void deleteByBook(Book book) {
        commentRepository.deleteByBook(book);
    }


    private Comment save(String id, String commentText, String bookId) {

        var book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book with id %s not found".formatted(bookId)));

        var comment = new Comment(id, book, commentText);
        return commentRepository.save(comment);
    }
}
