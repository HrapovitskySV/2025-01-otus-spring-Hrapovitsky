package ru.otus.hw.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.dto.CommentDto;

@Component
@RequiredArgsConstructor
public class CommentConverter {

    public String commentDtoToString(CommentDto comment) {
        return "Id: %d, Name: %s, BookId: %d".
                formatted(comment.getId(), comment.getComment(), comment.getBookId());
    }

    public CommentDto toDto(Comment comment) {
        var commentDto = new CommentDto();
        commentDto.setId(comment.getId());
        commentDto.setBookId(comment.getBook().getId());
        commentDto.setComment(comment.getComment());
        return commentDto;
    }
}
