package ru.otus.hw.processors;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import ru.otus.hw.modelsJpa.Book;
import ru.otus.hw.modelsMongo.BookMongo;
import ru.otus.hw.services.MapObjectService;

@RequiredArgsConstructor
public class BookItemProcessor implements ItemProcessor<BookMongo, Book> {

    private final MapObjectService mapObjectService;

    public Book process(final BookMongo bookMongo) {
        var author = mapObjectService.getTemplateAuthorFromKey(bookMongo.getAuthor().getId());
        var genres = bookMongo.getGenres().stream()
                .map(genreMongo -> mapObjectService.getTemplateGenreFromKey(genreMongo.getId())).toList();

        var book = new Book(0,bookMongo.getTitle(), author, genres);
        mapObjectService.putBook(bookMongo.getId(), book);
        return book;
    }
}