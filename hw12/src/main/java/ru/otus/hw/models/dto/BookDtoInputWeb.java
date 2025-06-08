package ru.otus.hw.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@Data
@AllArgsConstructor
public class BookDtoInputWeb {
    private long id;

    private String title;

    private long author;

    private Set<Long> genres;
}
