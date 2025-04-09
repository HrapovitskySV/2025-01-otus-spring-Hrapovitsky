package ru.otus.hw.models.dto;

import lombok.Data;
import ru.otus.hw.models.Author;

import java.math.BigInteger;
import java.util.List;

@Data
public class BookDto {
    private BigInteger id;

    private String title;

    private Author author;

    private List<GenreDto> genres;
}
