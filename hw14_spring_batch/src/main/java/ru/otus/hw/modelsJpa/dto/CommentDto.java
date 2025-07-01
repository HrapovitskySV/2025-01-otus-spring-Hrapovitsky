package ru.otus.hw.modelsJpa.dto;

import lombok.Data;

@Data
public class CommentDto {
    private long id;

    private long bookId;

    private String comment;
}
