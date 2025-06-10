package ru.otus.hw.models;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Data
@Document(collection = "books")
@AllArgsConstructor
@NoArgsConstructor
public class Book {
    @Id
    private String id;

    @Field(name = "title")
    private String title;

    private Author author;

    private List<Genre> genres;
}
