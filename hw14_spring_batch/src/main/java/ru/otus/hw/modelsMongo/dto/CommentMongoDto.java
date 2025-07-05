package ru.otus.hw.modelsMongo.dto;

import lombok.Data;
import ru.otus.hw.modelsMongo.BookMongo;

@Data
public class CommentMongoDto {
    private String id;

    private BookMongo book;

    private String comment;
}
