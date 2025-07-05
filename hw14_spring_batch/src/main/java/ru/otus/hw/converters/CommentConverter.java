package ru.otus.hw.converters;

import org.springframework.stereotype.Component;
import ru.otus.hw.modelsMongo.CommentMongo;
import ru.otus.hw.modelsMongo.dto.CommentMongoDto;

@Component
public class CommentConverter {
    public String commentToString(CommentMongo comment) {
        return "Id: %s, Name: %s, BookId: %s".
                formatted(comment.getId(), comment.getComment(), comment.getBook().getId());
    }

    public String commentDtoToString(CommentMongoDto comment) {
        return "Id: %s, Name: %s, BookId: %s".
                formatted(comment.getId(), comment.getComment(), comment.getBook().getId());
    }

    public CommentMongoDto toDto(CommentMongo comment) {
        var commentDto = new CommentMongoDto();
        commentDto.setId(comment.getId());
        commentDto.setBook(comment.getBook());
        commentDto.setComment(comment.getComment());
        return commentDto;
    }
}
