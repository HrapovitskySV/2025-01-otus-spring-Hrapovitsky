package ru.otus.hw.models.dto;

import lombok.Data;
import ru.otus.hw.models.Book;

@Data
public class CommentDto {
    private long id;

    private BookDto book;

    private String comment;
}
