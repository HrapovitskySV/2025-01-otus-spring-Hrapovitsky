package ru.otus.hw.processors;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import ru.otus.hw.modelsJpa.Comment;
import ru.otus.hw.modelsMongo.CommentMongo;
import ru.otus.hw.services.MapObjectService;

@RequiredArgsConstructor
public class CommentItemProcessor implements ItemProcessor<CommentMongo, Comment> {

    private final MapObjectService mapObjectService;

    @Override
    public Comment process(final CommentMongo commentMongo) {
        var bookId = mapObjectService.getIdBookFromKey(commentMongo.getBook().getId());

        return new Comment(0,bookId, commentMongo.getComment());
    }
}