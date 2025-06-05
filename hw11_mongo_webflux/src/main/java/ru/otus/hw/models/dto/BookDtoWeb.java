package ru.otus.hw.models.dto;

import lombok.Data;

@Data
public class BookDtoWeb {
    private String id;

    private String title;

    private String author;

    private String genres;
}
