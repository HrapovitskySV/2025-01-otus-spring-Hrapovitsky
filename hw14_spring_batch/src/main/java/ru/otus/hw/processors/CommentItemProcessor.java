package ru.otus.hw.processors;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.modelsJpa.Author;
import ru.otus.hw.modelsJpa.Comment;
import ru.otus.hw.modelsMongo.AuthorMongo;
import ru.otus.hw.modelsMongo.CommentMongo;
import ru.otus.hw.services.CountMapper;
import ru.otus.hw.services.MapObjectService;

import java.util.Map;

import static java.util.Objects.isNull;

@RequiredArgsConstructor
public class CommentItemProcessor implements ItemProcessor<CommentMongo, Comment> {

    private final MapObjectService mapObjectService;

    @Override
    public Comment process(final CommentMongo commentMongo) {
        var book_id = mapObjectService.getIdBookFromKey(commentMongo.getBook().getId());

        return new Comment(0,book_id, commentMongo.getComment());
    }
}