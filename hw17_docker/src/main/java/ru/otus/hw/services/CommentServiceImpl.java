package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.converters.CommentConverter;
import ru.otus.hw.exceptions.EntityNotFoundException;
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
    @Transactional(readOnly = true)
    public Optional<CommentDto> findById(long id) {
        Optional<Comment> oComment = commentRepository.findById(id);
        return oComment.map(commentConverter::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentDto> findByBookId(Long bookId) {
        var comments = commentRepository.findByBookId(bookId);
        return comments.stream().map(commentConverter::toDto).toList();
    }

    @Override
    @Transactional()
    public Comment insert(String comment, long bookId) {
        return save(0, comment, bookId);
    }

    @Override
    @Transactional()
    public Comment update(long id, String comment, long bookId) {
        return save(id, comment, bookId);
    }

    @Override
    @Transactional()
    public void deleteById(long id) {
        commentRepository.deleteById(id);
    }

    private Comment save(long id, String commentText, long bookId) {

        var book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book with id %d not found".formatted(bookId)));

        var comment = new Comment(id, book, commentText);
        return commentRepository.save(comment);
    }
}
