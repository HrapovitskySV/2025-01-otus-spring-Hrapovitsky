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
    public void deleteByBookId(String bookId) {
        //var book = bookRepository.findById(bookId)
        //        .orElseThrow(() -> new EntityNotFoundException("Book with id %d not found".formatted(bookId)));

        //var book = new Book(bookId,null,null,null);
        //commentRepository.deleteByBook(book);
        commentRepository.deleteByBookId(bookId);

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
