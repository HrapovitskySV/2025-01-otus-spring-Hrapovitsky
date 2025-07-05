package ru.otus.hw.processors;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.otus.hw.modelsJpa.Author;
import ru.otus.hw.modelsJpa.Genre;
import ru.otus.hw.modelsMongo.GenreMongo;
import ru.otus.hw.services.CountMapper;
import ru.otus.hw.services.MapObjectService;

import java.util.Map;

@RequiredArgsConstructor
public class GerneItemProcessor implements ItemProcessor<GenreMongo, Genre> {

    private final MapObjectService mapObjectService;

    @Override
    public Genre process(final GenreMongo genreMongo) {
        var genre = new Genre(0,genreMongo.getName());
        mapObjectService.putGenre(genreMongo.getId(), genre);
        return genre;
    }
}