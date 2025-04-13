package ru.otus.hw.models.dto;

import lombok.Data;

@Data
public class CommentDto {
    private long id;

    private BookDto book;

    private String comment;
}
