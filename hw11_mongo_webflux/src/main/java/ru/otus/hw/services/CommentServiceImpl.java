package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.otus.hw.converters.CommentConverter;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;

@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    private final BookRepository bookRepository;

    private final CommentConverter commentConverter;

    private Mono<Book> bookNotFound(String bookId) {
        throw new EntityNotFoundException("Book with id %s not found".formatted(bookId));
    }

    @Override
    public Mono<Comment> save(String id, String commentText, String bookId) {
        return bookRepository.findById(bookId)
                .switchIfEmpty(Mono.defer(() -> bookNotFound(bookId)))
                .flatMap(book -> {
                    if (book == null) {
                        throw new EntityNotFoundException("Book with id %s not found".formatted(bookId));
                    }
                    var comment = new Comment(id, book, commentText);
                    return commentRepository.save(comment);
                });

    }
}
