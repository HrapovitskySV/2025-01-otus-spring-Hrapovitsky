package ru.otus.hw.modelsMongo.dto;

import lombok.Data;
import ru.otus.hw.modelsMongo.AuthorMongo;

import java.util.List;

@Data
public class BookMongoDto {
    private String id;

    private String title;

    private AuthorMongo author;

    private List<GenreMongoDto> genres;
}
