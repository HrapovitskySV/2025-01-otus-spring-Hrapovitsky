package ru.otus.hw.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookDtoWeb {
    private long id;

    private String title;

    private String author;

    private String genres;
}
