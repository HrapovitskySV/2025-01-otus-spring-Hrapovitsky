package ru.otus.hw.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Book {
    private long id;

    private String title;

    private Author author;

    private List<Genre> genres;

    public void addGenre(Genre genre){
        if (Objects.isNull(genres)){
            genres=new ArrayList<Genre>();
        }
        genres.add(genre);
    }
}
