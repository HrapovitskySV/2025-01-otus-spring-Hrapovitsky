package ru.otus.hw.processors;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import ru.otus.hw.modelsJpa.Author;
import ru.otus.hw.modelsMongo.AuthorMongo;
import ru.otus.hw.services.MapObjectService;

@RequiredArgsConstructor
public class AuthorItemProcessor implements ItemProcessor<AuthorMongo, Author> {

    private final MapObjectService mapObjectService;



    @Override
    public Author process(final AuthorMongo authorMongo) {
        var author = new Author(0,authorMongo.getFullName());
        mapObjectService.putAuthor(authorMongo.getId(), author);
        return author;
    }
}