package ru.otus.hw.models.dto;

import lombok.Data;
import ru.otus.hw.models.Author;
import java.util.List;

@Data
public class BookDto {
    private String id;

    private String title;

    private Author author;

    private List<GenreDto> genres;
}
