package ru.otus.hw.models.dto;

import lombok.Data;
import ru.otus.hw.models.Book;

@Data
public class CommentDto {
    private String id;

    private Book book;

    private String comment;
}
