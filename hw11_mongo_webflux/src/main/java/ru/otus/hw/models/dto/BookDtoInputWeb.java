package ru.otus.hw.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@Data
@AllArgsConstructor
public class BookDtoInputWeb {
    private String id;

    private String title;

    private String author;

    private Set<String> genres;
}
