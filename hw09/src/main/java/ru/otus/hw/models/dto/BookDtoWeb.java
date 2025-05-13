package ru.otus.hw.models.dto;

import lombok.Data;

@Data
public class BookDtoWeb {
    private long id;

    private String title;

    private String author;

    private String genres;
}
