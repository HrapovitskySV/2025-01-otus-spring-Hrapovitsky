package ru.otus.hw.converters;

import org.springframework.stereotype.Component;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.dto.CommentDto;

@Component
public class CommentConverter {
    public String commentToString(Comment comment) {
        return "Id: %d, Name: %s, BookId: %d".
                formatted(comment.getId(), comment.getComment(), comment.getBook().getId());
    }

    public String commentDtoToString(CommentDto comment) {
        return "Id: %d, Name: %s, BookId: %d".
                formatted(comment.getId(), comment.getComment(), comment.getBook().getId());
    }

    public CommentDto toDto(Comment comment) {
        var commentDto = new CommentDto();
        commentDto.setId(comment.getId());
        commentDto.setBook(comment.getBook());
        commentDto.setComment(comment.getComment());
        return commentDto;
    }
}
